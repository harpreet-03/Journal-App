package com.happy.journalApp.controller;

import com.happy.journalApp.entity.JournalEntry;
import com.happy.journalApp.entity.User;
import com.happy.journalApp.service.JournalEntryService;
import com.happy.journalApp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userEntryService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found: " + userName, HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> journalEntryList = user.getJournalEntries();
        if(journalEntryList !=  null && !journalEntryList.isEmpty()) {
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/{userName}")
    public ResponseEntity<?>  createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {

        try{
            User user = userEntryService.findByUserName(userName);

            if (user == null) {
                return new ResponseEntity<>("User not found: " + userName, HttpStatus.NOT_FOUND);
            }
            myEntry.setJournalDate(LocalDateTime.now());
            journalEntryService.saveEntry( myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getEntry(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry =  journalEntryService.findById(myId);
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<?>  updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry, @PathVariable String userName) {
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null) {
            old.setJournalName(newEntry.getJournalName() != null && !newEntry.getJournalName().equals("") ? newEntry.getJournalName() : old.getJournalName());
            old.setJournalContent(newEntry.getJournalContent() != null && !newEntry.getJournalContent().equals("") ? newEntry.getJournalContent() : old.getJournalContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
