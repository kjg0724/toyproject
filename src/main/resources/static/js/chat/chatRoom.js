// https://www.hivemq.com/blog/mqtt-client-library-encyclopedia-paho-js/
//var userid = "user" + parseInt(Math.random() * 100, 10);
// var username = name + "(" + email + ")";
var userid = "User " + user_id;
//var user_id = "user"+$('#user_Id').val();
var reconnectTimeout = 2000;
var host = chat.mqtt.host;
var port = parseInt(chat.mqtt.port);
//var roomId = 1000000;
//var room_id = $('#room_Id').val();
var topic = "chat/room/" + room_id;
var clientId = "clientid_" + user_id;
var last_idx;

console.log("USER_ID :: " + userid);
console.log("TOPIC :: "+ topic);

$(function() {
    //Offline 상태 처리
    $(window).on('beforeunload',function (){
        var message = new Paho.MQTT.Message("!#Off_"+user_id);
        message.destinationName = topic;
        client.send(message);
        location.href=document.referrer;
        //sessionStorage.clear();
    })

    //메세지 삭제 클릭 이벤트
    $(document).on('click','button[name=deleteMsg]',function (e){
        var msg_idx = e.target.id.split("_")[1];
        //var cf = confirm("재접속 시, 메세지를 복구할 수 없습니다.\n 메세지를 삭제하시겠습니까?");
        var cf = confirm(alert_delete);
        if(cf){
            var url = "/chat/deleteMessage";
            $.ajax({
                url : url,
                type : 'POST',
                data : 'msg_idx='+msg_idx,
                dataType : 'json',
                success : function (result){
                    console.log(result);
                    var data = JSON.parse(result.data);
                    if(data[0].resultCode == 0){
                        var message = new Paho.MQTT.Message("!#Remove_"+msg_idx+"_"+user_id);
                        message.destinationName = topic;
                        client.send(message);
                    }
                },
                error : function (xhr){
                    console.log(xhr.status);
                }
            })
        }
    });

    //메세지 복구 클릭 이벤트
    $(document).on('click','button[name=restoreMsg]', function (e){
        var msg_idx = e.target.closest('div').id.split('_')[1];
        var url="/chat/restoreMessage"
        $.ajax({
            url : url,
            type : 'POST',
            data : 'msg_idx='+msg_idx,
            dataType : 'json',
            success : function (result){
                console.log(result);
                var data = JSON.parse(result.data);
                if(data[0].resultCode == 0){
                    var message = new Paho.MQTT.Message("!#Restore_"+msg_idx+"_"+user_id+"_"+data[0].msg);
                    message.destinationName = topic;
                    client.send(message);
                }
            },
            error : function (xhr){
                console.log(xhr.status);
            }
        })
        console.log(msg_idx);
    });


    //채팅방 탈퇴처리
    $('#deleteMyRoom').click(function (){
        //var result = confirm("정말로 방을 나가시겠습니까?");
        var result = confirm(alert_exit);
        if(result){
            var url="/chat/deleteMyRoom";
            var data = "room_id="+room_id+"&user_id="+user_id;
            $.ajax({
                url : url,
                type : 'Delete',
                data : data,
                dataType : 'json',
                success : function (result){
                    console.log(result.data);
                    $(location).attr("href","/chat/chatRoomList/"+user_id);
                },
                error : function (xhr){
                    console.log(xhr.status);
                }
            })
        }
    });

    //메세지 전송(마우스이벤트 바인딩)
    $('#btnSend').on('click', function(event) {
        event.preventDefault(); // To prevent following the link (optional)
        sendMessage($('#txtMessage').val());
        storeMessage($('#txtMessage').val());
        $('#txtMessage').val('');
    });

    //메세지 전송(키보드이벤트 바인딩)
    $('#txtMessage').on('keypress', function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            sendMessage($('#txtMessage').val());
            storeMessage($('#txtMessage').val());
            $('#txtMessage').val('');
        }
    })

    function sendMessage(msg) {
        var message = new Paho.MQTT.Message(JSON.stringify({
            userid : userid,
            email : email,
            message: msg,
            sendTime: new Date().getTime()
        }));
        message.destinationName = topic;
        client.send(message);
    }


// called when the client connects
    function onConnect() {
        // Once a connection has been made, make a subscription and send a message.
        console.log("onConnect");
        client.subscribe(topic, {
            onSuccess : function () {
                console.log("subscribe succeeded");
            },
            onFailure : function () {
                console.log("subscribe failed");
            }
        });
        //온라인 상태처리
        var message = new Paho.MQTT.Message("!#On_"+user_id);
        message.destinationName = topic;
        client.send(message);

        initUserList();
        //initUserList2();
        initChatRoom();
    }

// called when the client loses its connection
    function onConnectionLost(responseObject) {
        alert('exit');
        if (responseObject.errorCode !== 0) {
            console.log("onConnectionLost:" + responseObject.errorMessage);
        }
    }

// called when a message arrives
    function onMessageArrived(message) {
        console.log("onMessageArrived:" + message.payloadString);
        //메세지 삭제, 복구
        if(message.payloadString.indexOf("!#Remove") != -1){
            var msg_idx = message.payloadString.split("_")[1];
            var uId = message.payloadString.split("_")[2];
            if(uId == user_id){
                $('#msg_'+msg_idx).removeClass('bg-warning').addClass('bg-secondary').text("삭제된 메세지 입니다.");
                $('button').remove('#delMsg_'+msg_idx);
                $('#buttonArea_'+msg_idx).append('' +
                    '<button type="button" id="resMsg_'+msg_idx+'" name="restoreMsg" class="small btn btn-sm btn-success">복구</button>');
            }
            else{
                $('#msg_'+msg_idx).removeClass('bg-warning').addClass('bg-secondary').text("삭제된 메세지 입니다.");
                $('button').remove('#delMsg_'+msg_idx);
            }
        }
        else if(message.payloadString.indexOf("!#Restore") != -1){
            var msg_idx = message.payloadString.split("_")[1];
            var uId = message.payloadString.split("_")[2];
            var msg = message.payloadString.split("_")[3];
            if(uId == user_id){
                $('#msg_'+msg_idx).empty();
                $('#msg_'+msg_idx).removeClass('bg-secondary').addClass('bg-warning').append('' +
                    '<div class="font-weight-bold mb-1">나</div>'+msg);
                $('button').remove('#resMsg_'+msg_idx);
                $('#buttonArea_'+msg_idx).append('' +
                    '<button type="button" id="delMsg_'+msg_idx+'" name="deleteMsg" class="small btn btn-sm btn-danger">x</button>');
            }
            else{
                $('#msg_'+msg_idx).empty();
                $('#msg_'+msg_idx).append('' +
                    '<div class="font-weight-bold mb-1">User '+uId+'</div>'+msg);
            }
        }

        //온라인,오프라인 상태
        else if(message.payloadString.indexOf("!#On") != -1){
            var cid = message.payloadString.split("_")[1];
            //sessionStorage.setItem(cid,'Online');
            if(cid != user_id){
                var message = new Paho.MQTT.Message("!#Too_"+user_id);
                message.destinationName = topic;
                client.send(message);
                $('#status_'+cid).removeClass("chat-offline").addClass("chat-online");
                $('#status_'+cid).text('Online');
                initInOut(cid);
            }
        }
        else if(message.payloadString.indexOf("!#Off") != -1){
            var cid = message.payloadString.split("_")[1];
            //sessionStorage.setItem(cid,'Offline');
            if(cid != user_id){
                $('#status_'+cid).removeClass("chat-online").addClass("chat-offline");
                $('#status_'+cid).text('Offline');
                initInOut(cid);
            }
        }
        else if(message.payloadString.indexOf("!#Too") != -1){
            var cid = message.payloadString.split("_")[1];
            //sessionStorage.setItem(cid,'Online');
            $('#status_'+cid).removeClass("chat-offline").addClass("chat-online");
            $('#status_'+cid).text('Online');
        }

        //채팅 메세지
        else{
        var msg_idx = last_idx+1;
        var data = JSON.parse(message.payloadString);

        var now = new Date();
        var nowStr = (now.getHours() > 12 ? now.getHours() - 12 : now.getHours()) +
            ":" + now.getMinutes() +
            " " + (now.getHours() > 12 ? "pm" : "am");

        if (data.userid == userid) {
            $('#lstChat').append('<div class="chat-message-right pb-4">\n' +
                '                                <div id="buttonArea_'+msg_idx+'">\n' +
                '                                    <div class="text-muted small text-nowrap mt-2">'+nowStr+'</div>\n' +
                '                                    <button type="button" id="delMsg_'+msg_idx+'" name="deleteMsg" class="small btn btn-sm btn-danger">x</button>'+
                '                                </div>\n' +
                '                                <div id="msg_'+msg_idx+'" name="messageBox" class="flex-shrink-1 bg-warning rounded py-2 px-3 mr-3">\n' +
                '                                    <div class="font-weight-bold mb-1">나</div>\n' +
                '                                    ' + data.message +'\n' +
                '                                </div>\n' +
                '                            </div>')
            last_idx++;
        }
        else {
            $('#lstChat').append('<div class="chat-message-left pb-4">\n' +
                '                                <div>\n' +
                '                                    <div class="text-muted small text-nowrap mt-2">'+nowStr+'</div>\n' +
                '                                </div>\n' +
                '                                <div id="msg_'+msg_idx+'" name="messageBox" class="flex-shrink-1 bg-light rounded py-2 px-3 mr-3">\n' +
                '                                    <div class="font-weight-bold mb-1">' + data.userid + '</div>\n' +
                '                                    ' + data.message +'\n' +
                '                                </div>\n' +
                '                            </div>')
            last_idx++;
        }
        $('#lstChat').scrollTop($('#lstChat')[0].scrollHeight);
        }
    }

    function MQTTconnect() {
        $('#chatNav').load("/chat/getNav");
        console.log("connecting to " + host + " " + port)

        client = new Paho.MQTT.Client(host, port, "/ws", clientId);

        client.onConnectionLost = onConnectionLost;
        client.onMessageArrived = onMessageArrived;

        client.connect({
            onSuccess: onConnect,
            onFailure: function() { console.log("connection failed");}
        });
    }

    MQTTconnect();

    //유저 리스트 초기화
    function initUserList(){
        if(userList.length != 0){
            //console.log(sessionStorage.getItem(userList[0].user_id));
            for(i in userList){
                $('#userList').append(
                    '<a href="#" id="userBox_'+userList[i].user_id+'" name="userBox" class="list-group-item list-group-item-action border-0">'+
                    '<div class="d-flex align-items-start">' +
                    '<div id="statusDiv" class="flex-grow-1 ml-3 font-weight-bold">' +
                    'User '+userList[i].user_id + '('+userList[i].email+')'+
                    '<div class="small"><span id="status_'+userList[i].user_id+'" class="fas fa-circle chat-offline">Offline</span></div>' +
                    '</div>' +
                    '</div>' +
                    '</a>'
                )
            }
        }
    }

    //방 입장, 퇴장 처리
    function initInOut(cid){
        var url="/chat/getRoomUser";
        var len = $('a[name=userBox]').length;
        $.ajax({
            url : url,
            type : 'GET',
            data : 'room_id='+room_id+"&user_id="+user_id,
            dataType : 'json',
            success : function (result){
                var data = JSON.parse(result.data);
                if(data.length > len){
                    $('#userList').append(
                        '<a href="#" id="userBox_'+cid+'" name="userBox" class="list-group-item list-group-item-action border-0">'+
                        '<div class="d-flex align-items-start">' +
                        '<div id="statusDiv" class="flex-grow-1 ml-3 font-weight-bold">' +
                        'User '+cid + '('+data[data.length-1].email+')'+
                        '<div class="small"><span id="status_'+cid+'" class="fas fa-circle chat-online">Online</span></div>' +
                        '</div>' +
                        '</div>' +
                        '</a>'
                    )
                    $('#lstChat').append('<h5 class="text-center">User '+cid+'님이 입장하셨습니다.</h5>');
                }
                else if(data.length < len){
                    $('#userBox_'+cid).remove();
                    $('#lstChat').append('<h5 class="text-center">User '+cid+'님이 퇴장하셨습니다.</h5>');
                }
            },
            error : function (xhr){
                console.log(xhr.status);
            }

        })
    }

    //메세지 저장
    function storeMessage(message){
        console.log("ROOM_ID :: " + room_id);
        console.log("CLIENT_ID :: " + clientId);
        console.log("MESSAGE :: " + message);
        var url = "/chat/storeMessage"
        var data = "room_id="+room_id+"&user_id="+user_id+"&msg="+message;
        $.ajax({
            url : url,
            type : 'PUT',
            data : data,
            success : function (result){
                console.log(result);
            },
            error : function (xhr){
                console.log(xhr.status);
            }
        })
    }

    //채팅방(메세지) 초기화
    function initChatRoom(){
        var url = "/chat/getChatMessage";
        var data = "room_id="+room_id+"&user_id="+user_id;
        var email =
        $.ajax({
            url : url,
            type: 'GET',
            data : data,
            dataType : 'json',
            success : function (result){
                var data = JSON.parse(result.data);
                last_idx = data[data.length-1].msg_idx;
                for(i in data){
                    var now = data[i].last_msg_dt.split(" ")[1];
                    var hour = now.split(":")[0];
                    var min = now.split(":")[1];
                    var nowStr = (hour > 12 ? hour-12 : hour) +":"+min+(hour > 12 ? "pm" : "am");
                    if(data[i].room_id == room_id){
                        if(data[i].del_yn == '0'){
                            if (data[i].user_id == user_id) {
                                $('#lstChat').append('<div class="chat-message-right pb-4">\n' +
                                    '                                <div id="buttonArea_'+data[i].msg_idx+'">\n' +
                                    '                                    <div class="text-muted small text-nowrap mt-2">'+nowStr+'</div>' +
                                    '                                    <button id="delMsg_'+data[i].msg_idx+'" name="deleteMsg" type="button" class="small btn btn-sm btn-danger">x</button>'+
                                    '                                </div>\n' +
                                    '                                <div id="msg_'+data[i].msg_idx+'" name="messageBox" class="flex-shrink-1 bg-warning rounded py-2 px-3 mr-3">\n' +
                                    '                                    <div class="font-weight-bold mb-1">나</div>\n' +
                                    '                                    ' + data[i].msg +'\n' +
                                    '                                </div>\n' +
                                    '                            </div>')
                            }
                            else {
                                $('#lstChat').append('<div class="chat-message-left pb-4">\n' +
                                    '                                <div>\n' +
                                    '                                    <div class="text-muted small text-nowrap mt-2">'+nowStr+'</div>\n' +
                                    '                                </div>\n' +
                                    '                                <div id="msg_'+data[i].msg_idx+'" name="messageBox" class="flex-shrink-1 bg-light rounded py-2 px-3 mr-3">\n' +
                                    '                                    <div class="font-weight-bold mb-1">User ' + data[i].user_id + '</div>\n' +
                                    '                                    ' + data[i].msg +'\n' +
                                    '                                </div>\n' +
                                    '                            </div>')
                            }
                        }
                        $('#lstChat').scrollTop($('#lstChat')[0].scrollHeight);
                    }
                }
            },
            error : function(xhr){
                console.log(xhr.status);
            }
        })

    }
})