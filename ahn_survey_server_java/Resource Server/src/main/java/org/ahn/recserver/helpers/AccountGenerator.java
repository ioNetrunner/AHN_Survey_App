/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Stores frequent queries in memory
 *
 * @author rgustafs
 */
public class AccountGenerator {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final int PWLEN = 12;

    private final ArrayList<String> nouns;
    private final ArrayList<String> adj;
    private final ArrayList<String> animals;

    public AccountGenerator(List<String> nouns, List<String> adj, List<String> animals) {
        this.nouns = new ArrayList<>(nouns);
        this.adj = new ArrayList<>(adj);
        this.animals = new ArrayList<>(animals);
    }

    /**
     * Generate unique username
     *
     * @return new username or null if couldn't generate
     */
    public String generateUsername() {
        Random rnd = new Random(System.currentTimeMillis());
        String username;
        for (int i = 0; i < 100; i++) {
            username = getString(rnd, adj, true) + getString(rnd, animals, true) + getNumber(rnd, 2);
            if (jdbcTemplate.queryForObject(
                    "SELECT count(*) FROM Auth WHERE Username = ?",
                    Integer.class,
                    username) == 0) {
                return username;
            }
        }
        return null;
    }

    public String generatePassword() {
        Random rnd = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();

        Integer count = 0;
        Integer[] len = {3, 4, 5};
        Integer flip = rnd.nextInt(1);

        while (sb.length() < PWLEN) {
            if ((flip == 0 && count % 2 == 0)
                    || (flip == 1 && count % 2 == 1)) {
                sb.append(getString(rnd, nouns, false));
            } else {
                sb.append(getNumber(rnd, len[rnd.nextInt(2)]));
            }
        }

        return sb.toString();
    }

    private static String getString(Random rnd, ArrayList<String> words, boolean capitalize) {
        String str = words.get(rnd.nextInt(words.size() - 1)).replace("\n", "");

        return (capitalize)
                ? (str.substring(0, 1).toUpperCase() + str.substring(1))
                : str;
    }

    private static String getNumber(Random rnd, Integer Length) {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < Length; i++) {
            num.append(rnd.nextInt(9));
        }
        return num.toString();
    }

}
