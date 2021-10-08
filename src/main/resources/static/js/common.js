$(document).ready(function (){
    $('#chatNav').load("/chat/getNav");

    // SignIn Click Event
    $('#form-signIn').on('keypress', function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            signIn();
        }
    });
    $('#signInSubmit').on('click',function (){
        signIn();
    })

    // SignUp Click Event
    $('#form-signUp').on('keypress', function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            signUp();
        }
    });
    $('#signUpSubmit').on('click',function (){
        signUp();
    })

    // DropOut Click Event
    $('#form-dropOut').on('keypress', function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            doDropOut();
        }
    });
    $('#dropOutSubmit').on('click',function (){
        doDropOut();
    })

    // Create Chat Room Click Event
    $('#makeRoom').click(function makeRoom(){
        var room_nm = prompt(prompt_makeRoom);
        if(room_nm === null){
            return;
        }
        if(room_nm.charAt(1) == ''){
            alert(alert_roomNm);
            makeRoom();
            return;
        }
        $('#room_nm').val(room_nm);

        makeChatRoom();
    });

    // Move ChatRoom List Event
    function goChatRoomList(id){
        $(location).attr("href","/chat/chatRoomList/"+id);
    }

    // Add MyRoom
    function addMyRoom(room_id, user_id, email){
        var url = "/chat/addMyRoom";
        $.ajax({
            url : url,
            type : 'PUT',
            data : 'room_id='+room_id+"&user_id="+user_id+"&email="+email,
            success : function (result){
                console.log(result);
                location.reload();
            },
            error : function (xhr){
                console.log(xhr.status);
            }
        })
    }

    // Create ChatRoom
    function makeChatRoom(){
        var queryString = $('form[name=makeRoomForm]').serialize();
        var url = "/chat/makeChatRoom";
        $.ajax({
            url : url,
            type : 'PUT',
            data : queryString,
            dataType : 'json',
            success : function (result){
                console.log(result);
                location.reload();
            },
            error : function (xhr){
                console.log(xhr.status);
            }
        });
    }

    // SignIn
    function signIn(){
        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if($('#inputEmail').val()==''){
            alert(alert_id);
        }
        else{
            if(!$('#inputEmail').val().match(regExp)) {
                alert(alert_invalid_email);
            }
            else if($('#inputPassword').val()==''){
                alert(alert_pwd);
            }
            else{
                var url = "/account/doSignIn";
                var queryString = $('form[name=signInForm]').serialize();
                $.ajax(
                    {
                        url : url,
                        type : 'POST',
                        data : queryString,
                        dataType: 'json',
                        success : function(result){
                            console.log(result);
                            var data = JSON.parse(result.data);
                            if(data[0].resultCode == -1){
                                alert(alert_ep_mismatch);
                            }
                            else if(data[0].resultCode == -2){
                                alert(alert_not_exist);
                            }
                            else{
                                goChatRoomList(data[0].id);
                            }
                        },
                        error : function(xhr){
                            console.log(xhr.status)
                        }
                    }
                )
            }
        }
    }

    // SignUp
    function signUp(){
        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if($('#inputEmail').val()==''){
            alert(alert_id);
        }
        else{
            if(!$('#inputEmail').val().match(regExp)) {
                alert(alert_invalid_email);
            }
            else if($('#inputPassword').val()==''){
                alert(alert_pwd);
            }
            else{
                var url = "/account/doSignUp";
                var queryString = $('form[name=signUpForm]').serialize();
                $.ajax({
                    url : url,
                    type : 'PUT',
                    data : queryString,
                    dataType: 'json',
                    success : function (result){
                        console.log(result);
                        var data = JSON.parse(result.data);
                        if(data[0].resultCode === 0){
                            alert(alert_su_success)
                            $(location).attr('href','/account/signIn');
                        }else{
                            alert(alert_duplicated);
                        }

                    },
                    error : function (xhr){
                        console.log(xhr.status);
                    }
                })
            }
        }
    }

    // DropOut
    function doDropOut(){
        var url = "/account/doDropOut";
        var queryString = $('form[name=dropOutForm]').serialize();
        $.ajax({
            url : url,
            type : 'DELETE',
            data : queryString,
            dataType : 'json',
            success : function (result){
                console.log(result);
                console.log(result.data);
                var data = JSON.parse(result.data);
                if(data[0].resultCode == 0){
                    alert(alert_wd_success);
                    $(location).attr("href","/account/signIn");
                }
                else if(data[0].resultCode == -2){
                    alert(alert_pwd_mismatch);
                }
            },
            error : function (xhr){
                console.log(xhr.status);
            }
        })
    }
})


