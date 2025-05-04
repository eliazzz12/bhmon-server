package org.bhmon.server.io.persistance;

import org.bhmon.server.io.persistance.exceptions.BHMonDAOException;
import org.bhmon.server.io.persistance.exceptions.UserNotFoundException;
import org.bhmon.server.io.persistance.exceptions.UsernameTakenException;
import org.bhmon.server.model.Match;
import org.bhmon.server.model.user.User;

import java.util.List;

public interface BHMonDAO {
    boolean register(User user) throws UsernameTakenException, BHMonDAOException;
    boolean login(User user) throws UserNotFoundException, BHMonDAOException;
    boolean saveMatch(Match match) throws BHMonDAOException;
    List<String> getLeaderboard() throws BHMonDAOException;
    List<String> getResults(String username) throws BHMonDAOException;
    List<User> getUserList() throws BHMonDAOException;
    List<String> getUsernamesList() throws BHMonDAOException;
}
