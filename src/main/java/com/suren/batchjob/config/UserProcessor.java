package com.suren.batchjob.config;

import com.suren.batchjob.entity.Users;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<Users,Users> {
    @Override
    public Users process (Users users) throws Exception {

        users.setFirstName(users.getFirstName().toUpperCase());
        users.setLastName(users.getLastName().toUpperCase());

        return users;
    }
}
