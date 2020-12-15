/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Users;

/**
 *
 * @author saby_
 */
public class Mantainer implements User {
    private final String username;
    private final String password;

    public Mantainer(String username, String password) {
        this.username=username;
        this.password=password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getRole() {
        return "Maintainer";
    }
    
    
}
