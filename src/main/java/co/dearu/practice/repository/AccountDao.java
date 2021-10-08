package co.dearu.practice.repository;

import co.dearu.practice.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountDao extends MySqlRepositoryBase {
    List<User> getUserList();
    User getUser(String email);
    User getUserById(int id);
    int getUserCount(String email);
    void addUser(String email, String password, String name);
    int deleteUser(int id);
}
