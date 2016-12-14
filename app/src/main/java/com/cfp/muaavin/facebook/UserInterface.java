package com.cfp.muaavin.facebook;

import java.util.ArrayList;

/**
 *
 */
public interface UserInterface {

    void getReportedFriends(ArrayList<Friend> Friends);
    void getBlockedUsers(ArrayList<String> UserIds);
}
