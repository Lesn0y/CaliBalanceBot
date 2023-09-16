package org.lesnoy.user;

import org.lesnoy.exeptions.WebApiExeption;

public class UserService {
    private final UserWebService webService = new UserWebService();

    public UserDTO getUserStats(String userName) throws WebApiExeption {
        return webService.getUserStats(userName);
    }

    public UserDTO registerUser(UserDTO userDTO) throws WebApiExeption {
        return webService.registerUser(userDTO);
    }
}
