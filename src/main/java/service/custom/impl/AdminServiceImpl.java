package service.custom.impl;

import dto.Admin;
import entity.AdminEntity;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.AdminDao;
import service.custom.AdminService;
import util.DaoType;

public class AdminServiceImpl implements AdminService {

    private final AdminDao dao = DaoFactory.getInstance().getDaoType(DaoType.ADMIN);
    private final ModelMapper modelMapper = new ModelMapper();

    private final String encryptionKey = "1234";
    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public AdminServiceImpl() {
        encryptor.setPassword(encryptionKey);  // Set encryption key
    }

    @Override
    public boolean registerAdmin(String userName, String email, String password) {
        System.out.println("Service: " + userName + ", " + email);

        Admin adminDTO = new Admin(userName, email, password);
        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);

        if (dao.findByEmail(email) != null) {
            return false;
        }

        String encryptedPassword = encryptPassword(password);
        adminEntity.setPassword(encryptedPassword);

        return dao.save(adminEntity);
    }

    @Override
    public boolean authenticate(String email, String password) {
        AdminEntity admin = dao.findByEmail(email);
        if (admin != null) {
            String decryptedPassword = decryptPassword(admin.getPassword());
            return decryptedPassword.equals(password);
        }
        return false;
    }

    private String encryptPassword(String password) {
        return encryptor.encrypt(password);
    }

    private String decryptPassword(String encryptedPassword) {
        return encryptor.decrypt(encryptedPassword);
    }

    @Override
    public boolean checkEmailExists(String email) {
        AdminEntity adminEntity = dao.findByEmail(email);
        return adminEntity != null;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        AdminEntity adminEntity = dao.findByEmail(email);
        if (adminEntity != null) {
            String encryptedPassword = encryptPassword(newPassword);
            adminEntity.setPassword(encryptedPassword);

            return dao.update(adminEntity);
        }
        return false;
    }

    @Override
    public String getAdminName(String email) {
        AdminEntity adminEntity = dao.findByEmail(email);

        if (adminEntity != null) {
            return adminEntity.getUserName();
        }

        return "";
    }

}
