package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.services.UserService;

@Controller
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("users", users);
        return "admin/user/table-user";
    }
    @GetMapping("/admin/user/create")
     public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }
    @PostMapping("/admin/user/create")
     public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit) {
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
