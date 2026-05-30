package com.happy.journalApp.service;

import com.happy.journalApp.entity.JournalEntry;
import com.happy.journalApp.entity.User;
import com.happy.journalApp.repository.JournalEntryRepository;
import com.happy.journalApp.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserEntryService {

    @Autowired
    private UserEntryRepository userEntryRepository;

    public void saveEntry(User user) {
        userEntryRepository.save(user);
    }
    public List<User> findAll() {
        return userEntryRepository.findAll();
    }
    public Optional<User> findById(ObjectId id) {
        return userEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
         userEntryRepository.deleteById(id);
    }

    public User findByUserName(String userName) {
        return userEntryRepository.findByUserName(userName);
    }
}

// controller ---> service ---> repository