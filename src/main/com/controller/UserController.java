package com.controller;

import com.dao.UserDaoImpl;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());

    private UserService userService;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user){

        LOGGER.info(user.toString());
        if(user.getName() != null){
            this.userService.addUser(user);
        }else {
            this.userService.updateUser(user);
        }

        return "redirect:/users";
    }

    @RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") int id){
        this.userService.removeUser(id);

        return "redirect:/users";
    }



@RequestMapping("edit/{id}")
public ModelAndView editUser(@PathVariable("id") int id, ModelAndView modelAndView){
    modelAndView.setViewName("users");
    modelAndView.addObject("user", this.userService.getUserById(id));
    List<User> users = this.userService.listUsers();
    PagedListHolder<User> pagedListHolder = new PagedListHolder<User>(users);
    pagedListHolder.setPageSize(5);

    modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
    modelAndView.addObject("listUsers", pagedListHolder);

    return modelAndView;
}

    @RequestMapping("userdata/{id}")
    public String userData(@PathVariable("id") int id, Model model){
        model.addAttribute("user", this.userService.getUserById(id));

        return "userdata";
    }


    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView listUsers(@RequestParam(required = false) Integer page, ModelAndView modelAndView){

        modelAndView.setViewName("users");
        modelAndView.addObject("user", new User());
        List<User> users = this.userService.listUsers();
        PagedListHolder<User> pagedListHolder = new PagedListHolder<User>(users);
        pagedListHolder.setPageSize(5);
        modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
        modelAndView.addObject("listUsers", pagedListHolder);
        modelAndView.addObject("page", page);

        if(page==null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
        }
        else if (page <= pagedListHolder.getPageCount()){
            pagedListHolder.setPage(page-1);
        }

        return modelAndView;
    }

    @RequestMapping("/searchUser")
    public ModelAndView searchUser (@RequestParam(required = false, defaultValue = "") String name){

        ModelAndView mav = new ModelAndView ("users");
        mav.addObject("user", new User());

        List users = this.userService.searchUser(name);

        for(Object user: users) {
            LOGGER.info(user.toString());
        }

        PagedListHolder<User> pagedListHolder = new PagedListHolder<User>(users);
        pagedListHolder.setPageSize(5);
        mav.addObject("maxPages", pagedListHolder.getPageCount());
        mav.addObject("listUsers", pagedListHolder);

        return mav;
    }

}
