package org.lesnoy.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lesnoy.entry.Entry;

public class UserEntryDTO {
    private User user;
    @JsonProperty("last_entry")
    private Entry lastEntry;

    public UserEntryDTO() {
    }

    public UserEntryDTO(User user, Entry lastEntry) {
        this.user = user;
        this.lastEntry = lastEntry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entry getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(Entry lastEntry) {
        this.lastEntry = lastEntry;
    }
}
