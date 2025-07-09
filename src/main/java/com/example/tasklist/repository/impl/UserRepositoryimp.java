package com.example.tasklist.repository.impl;

import com.example.tasklist.domain.exception.ResourceMappingException;
import com.example.tasklist.domain.user.Role;
import com.example.tasklist.domain.user.User;
import com.example.tasklist.repository.DataSourceConfig;
import com.example.tasklist.repository.UserRepository;
import com.example.tasklist.repository.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryimp implements UserRepository  {

    private final DataSourceConfig dataSourceConfig;
    private final String FIND_BY_ID = """
                    SELECT u.id as user_id,
                                   u.name as user_name,
                                   u.username as user_username,
                                   u.password as user_password,
                                   ur.role as user_role_role,
                                   t.id as task_id,
                                   t.title as task_title,
                                   t.description as task_description,
                                   t.expiration_date as task_expiration_date,
                                   t.status as task_status
                             FROM taskList.users u
                                left join taskList.users_roles ur on u.id = ur.user_id
                                left join taskList.users_tasks ut on u.id = ut.user_id
                                left join taskList.tasks t on ut.task_id = t.id
                             where u.id = ?""";

    private final String FIND_BY_USERNAME = """
                    SELECT u.id as user_id,
                                   u.name as user_name,
                                   u.username as user_username,
                                   u.password as user_password,
                                   ur.role as user_role_role,
                                   t.id as task_id,
                                   t.title as task_title,
                                   t.description as task_description,
                                   t.expiration_date as task_expiration_date,
                                   t.status as task_status
                             FROM taskList.users u
                                left join taskList.users_roles ur on u.id = ur.user_id
                                left join taskList.users_tasks ut on u.id = ut.user_id
                                left join taskList.tasks t on ut.task_id = t.id
                             where u.username = ?""";

    private final String UPDATE = """
                    UPDATE taskList.users
                    SET name = ?,
                        username = ?,
                        password = ?
                    WHERE id = ?""";

    private final String CREATE = """
                    INSERT INTO taskList.users(name, username, password)
                    VALUES(?, ?, ?)""";

    private final String INSERT_USER_ROLE = """
                    INSERT INTO taskList.users_roles (user_id, role)
                    VALUES(?, ?)""";

    private final String IS_TASK_OWNER = """
            select exists(
            	select 1
            	from taskList.users_tasks
            	where user_id = ?
            		and task_id = ?
            )
                    WHERE id = ?""";

    private final String DELETE = """
                    DELETE FROM taskList.user
                    WHERE id = ?""";

    @Override
    public Optional<User> findById(Long id) {
        log.info("############### findById: id = " + id);
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
                    );
            statement.setLong(1, id);
            try {
                ResultSet rs = statement.executeQuery();
                log.info("############### findById: rs.getRow() = " + rs.getRow());
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );
            statement.setString(1, username);
            try {
                ResultSet rs = statement.executeQuery();
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while create user.");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while checking is task owner.");
        }
    }

    @Override
    public void delete(long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while delete user.");
        }

    }
}
