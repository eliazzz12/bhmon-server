package org.bhmon.server.io.persistance;

import org.bhmon.server.io.persistance.exceptions.BHMonDAOException;
import org.bhmon.server.io.persistance.exceptions.UserNotFoundException;
import org.bhmon.server.io.persistance.exceptions.UsernameTakenException;
import org.bhmon.server.model.Match;
import org.bhmon.server.model.user.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BHMonDAO_DB_MySQL implements BHMonDAO{
    private static BHMonDAO_DB_MySQL instance;
    private static final String DB_AUTH_PATH = "/secrets/db_connection.csv";
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    private BHMonDAO_DB_MySQL(String URL, String USERNAME, String PASSWORD) {
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }
    public static BHMonDAO_DB_MySQL getInstance() {
        if (instance == null) {
            final String [] CONN_INFO = getConnInfo(DB_AUTH_PATH);
            instance = new BHMonDAO_DB_MySQL(CONN_INFO[0], CONN_INFO[1], CONN_INFO[2]);
        }
        return instance;
    }

    /**
     * Returns an array containing the information required to connect to a database<br>
     * (url, user name and password)
     * @param PATH the path to the csv file containing the required information
     * @return an {@code String[]} with the structure [url, user_name, password]
     * @throws BHMonDAOException if a database access error occurs or this method is called on a closed connection
     */
    static String[] getConnInfo(final String PATH) throws BHMonDAOException {
        String [] array;
        try(BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line = br.readLine();
            array = line.split(",");
        } catch (FileNotFoundException e) {
            throw new BHMonDAOException(e.getMessage(), e);
        } catch (IOException e) {
            throw new BHMonDAOException(e.getMessage(), e);
        }

        return array;
    }

    @Override
    public boolean register(User user) throws UsernameTakenException, BHMonDAOException {
        return false;
    }

    @Override
    public boolean login(User user) throws UserNotFoundException, BHMonDAOException {
        return false;
    }

    @Override
    public boolean saveMatch(Match match) throws BHMonDAOException {
        return false;
    }

    @Override
    public List<String> getLeaderboard() throws BHMonDAOException {
        return List.of();
    }

    @Override
    public List<String> getResults(String username) throws BHMonDAOException {
        return List.of();
    }

    @Override
    public List<User> getUserList() throws BHMonDAOException {
        return List.of();
    }

    @Override
    public List<String> getUsernamesList() throws BHMonDAOException {
        return List.of();
    }
}
