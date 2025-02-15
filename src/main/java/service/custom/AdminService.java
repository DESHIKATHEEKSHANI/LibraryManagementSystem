package service.custom;

import service.SuperService;

public interface AdminService extends SuperService {
    boolean registerAdmin(String userName, String email, String password);
    boolean authenticate(String email, String password);
    boolean checkEmailExists(String email);
    boolean resetPassword(String email, String newPassword);

}
