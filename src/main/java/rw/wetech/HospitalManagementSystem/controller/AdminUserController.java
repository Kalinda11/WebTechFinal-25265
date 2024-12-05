package rw.wetech.HospitalManagementSystem.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rw.wetech.HospitalManagementSystem.model.Role;
import rw.wetech.HospitalManagementSystem.model.User;
import rw.wetech.HospitalManagementSystem.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000")

public class AdminUserController {

    private final UserService userService; // Assuming you have a UserService to handle user operations
//    private final FurnitureService furnitureService;

    public AdminUserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/admin/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User()); // Create a new User object
        return "add-user"; // Return the add user template
    }

    @PostMapping("/admin/users")
    public String addUser (@ModelAttribute User user) {
        userService.registerUser (user); // Save the user using your service
        return "redirect:/admin"; // Redirect to the admin dashboard after saving
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser (@PathVariable Long id) {
        userService.deleteUser (id); // Call the service to delete the user
        return "redirect:/admin"; // Redirect to the admin dashboard after deletion
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id); // Fetch the user by ID
        model.addAttribute("user", user); // Add the user to the model
        return "edit-user"; // Return the edit user template
    }

    @PostMapping("/admin/users/update")
    public String updateUser (@ModelAttribute User user) {
        userService.updateUser (user); // Call the service to update the user
        return "redirect:/admin"; // Redirect to the admin dashboard after updating
    }

    @GetMapping("/admin/search")
    public String showSearchForm() {
        return "search-user"; // Return the search user template
    }

    @GetMapping("/admin/search/results")
    public String searchUsers(@RequestParam(required = false) String username,
                              @RequestParam(required = false) String email,
                              Model model) {
        List<User> users = userService.searchUsers(username, email); // Call the service to search for users
        model.addAttribute("users", users); // Add the list of users to the model
        return "user-list"; // Return the template that displays the list of users
    }


    @GetMapping("/admin/download/users")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
        List<User> users = userService.getAllUsers(); // Fetch all users from the service

        // Create CSV content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Write CSV header
        writer.println("ID,Username,Email"); // Adjust according to your User fields

        // Write user data
        for (User  user : users) {
            writer.printf("%d,%s,%s%n", user.getId(), user.getUsername(), user.getEmail()); // Adjust according to your User fields
        }
        writer.flush();
        writer.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        // Set the content type and attachment header
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }



    @GetMapping("/admin/upload/furniture")
    public String showUploadForm() {
        return "upload"; // Return the upload form template
    }
    @GetMapping("/admin/upload")
    public String showUploadPage() {
        return "upload"; // Return the combined upload page template
    }
    @PostMapping("/admin/upload/users")
    public String uploadUsers(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("userMessage", "Please select a file to upload.");
            return "upload"; // Return to the upload page
        }

        try {
            List<User> userList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                reader.readLine(); // Skip header line

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 7) { // Validate input length
                        model.addAttribute("userMessage", "Invalid data format.");
                        return "upload"; // Return to the upload page
                    }

                    User user = new User();
                    user.setUsername(data[0]);
                    user.setFirstName(data[1]);
                    user.setLastName(data[2]);
                    user.setEmail(data[3]);
                    user.setPhoneNumber(data[4]);
                    user.setProfilePicture(data[5]);

                    // Handle potential IllegalArgumentException for role
                    try {
                        user.setRole(Role.valueOf(data[6].toUpperCase())); // Assuming roles are uppercase
                    } catch (IllegalArgumentException e) {
                        model.addAttribute("userMessage", "Invalid role: " + data[6]);
                        return "upload"; // Return to the upload page
                    }

                    userList.add(user);
                }
            }


            userService.saveAll(userList);

            model.addAttribute("userMessage", "Users uploaded successfully.");
        } catch (IOException e) {
            model.addAttribute("userMessage", "Error reading file: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("userMessage", "An unexpected error occurred: " + e.getMessage());
        }

        return "upload"; // Return to the upload page
    }
    @GetMapping("/admin/user-role-stats")
    @ResponseBody
    public Map<String, Integer> getUserRoleStatistics() {
        List<User> users = userService.getAllUsers(); // Fetch all users from the service
        Map<String, Integer> roleStats = new HashMap<>();

        // Count users per role
        for (User  user : users) {
            String role = user.getRole().name(); // Assuming getRole() returns a Role enum
            roleStats.put(role, roleStats.getOrDefault(role, 0) + 1);
        }

        return roleStats; // Return the statistics as a JSON response
    }

}