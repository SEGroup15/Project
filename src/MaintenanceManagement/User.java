/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaintenanceManagement;

/**
 *
 * @author saby_
 */
public class User {
    private String username;
    private String password;
    private String role;
    
    public User (String username,String password,String role) { 
    this.username=username;
    this.password=password;
    this.role=role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    
    
}
