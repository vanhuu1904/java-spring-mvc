package vn.hoidanit.laptopshop.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.services.UploadService;
import vn.hoidanit.laptopshop.services.UserService;

@Controller
public class UserController {

    private final UserService userService;
     private final UploadService uploadService;
     private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/")
    public String getHomePage(Model model){
        List<User> arrUser = this.userService.getAllUsersByEmail("huynam569@gmail.com");
        System.out.println(">>list user: " + arrUser);
        String test = this.userService.handleHello();
        model.addAttribute("eric", test);
        return "hello";
    }
    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        System.out.println(">>>chgcek user: " + users);
        model.addAttribute("users", users);
        return "admin/user/show";
    }
    @GetMapping("/admin/user/create")
     public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }
    @PostMapping("/admin/user/create")
     public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit, @RequestParam("hoidanitFile") MultipartFile file) {
         
         String avatar =  this.uploadService.handleSaveUploadFile(file, "avatar");
         
         String hashPassword = this.passwordEncoder.encode(hoidanit.getPassword());
         
         hoidanit.setAvatar(avatar);
         hoidanit.setPassword(hashPassword);
         hoidanit.setRole(this.userService.getRoleByName(hoidanit.getRole().getName()));
         this.userService.handleSaveUser(hoidanit);
      return "redirect:/admin/user";
    }
    @GetMapping(value = "/admin/user/{id}")
     public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/show";
    }
    @GetMapping(value = "/admin/user/update/{id}")
     public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser= this.userService.getUserById(id);
        model.addAttribute("updateUser", currentUser);
        return "admin/user/update";
    }


    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("updateUser") User userUpdate){
        User currentUser = this.userService.getUserById(userUpdate.getId());
        if(currentUser != null) {
            currentUser.setAddress(userUpdate.getAddress());
            currentUser.setFullName(userUpdate.getFullName());
            currentUser.setPhone(userUpdate.getPhone());
            this.userService.handleSaveUser(currentUser);
        }
        return  "redirect:/admin/user";
    }
    @GetMapping("/admin/user/delete/{id}")
     public String getDeleteUserPage(Model model, @PathVariable long id) {
         model.addAttribute("id", id);
        model.addAttribute("deleteUser", new User());
        return "admin/user/delete";
    }
    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("deleteUser") User user) {
        System.out.println(">>>>>>check user delete: " + user.toString());
        // this.userService.deleteUserById(user.getId());
        return "redirect:/admin/user";
    }
}
