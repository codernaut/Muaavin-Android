package com.cfp.muaavin.facebook;

import com.cfp.muaavin.web.User;

import java.util.ArrayList;

/**
 *
 */
public interface UserInterface {

    void getReportedFriends(ArrayList<String> Friends);
    void getBlockedUsers(ArrayList<String> UserIds);
}
