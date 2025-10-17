<%@page import="model.Car"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) request.getAttribute("user");
    List<Car> cars = (List<Car>) request.getAttribute("cars");
    String search = (String) request.getAttribute("search");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Profile - Car Management</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            
            html, body {
                height: 100%;
                font-family: Inter, "Segoe UI", Roboto, Arial, sans-serif;
                background: #f5f7fb;
                color: #111827;
            }

            .app {
                display: flex;
                height: 100vh;
            }

            /* Sidebar */
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg, #0f2340, #0b1830);
                color: #fff;
                padding: 28px 18px;
                display: flex;
                flex-direction: column;
            }
            
            .brand {
                font-weight: 800;
                font-size: 18px;
                letter-spacing: 1px;
                margin-bottom: 22px;
            }
            
            .nav {
                margin-top: 12px;
                display: flex;
                flex-direction: column;
                gap: 8px;
            }
            
            .nav a {
                color: rgba(255, 255, 255, 0.9);
                text-decoration: none;
                padding: 10px 12px;
                border-radius: 10px;
                display: flex;
                align-items: center;
                gap: 12px;
                transition: all 0.3s;
            }
            
            .nav a.active,
            .nav a:hover {
                background: rgba(255, 255, 255, 0.1);
            }
            
            .nav a i {
                width: 20px;
                text-align: center;
            }

            /* Main Content */
            .main {
                flex: 1;
                padding: 24px 32px;
                overflow: auto;
            }

            /* Profile Header Card */
            .profile-card {
                background: #fff;
                border-radius: 12px;
                padding: 30px;
                margin-bottom: 24px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
                display: flex;
                align-items: center;
                gap: 30px;
                position: relative;
            }
            
            .profile-image {
                position: relative;
            }
            
            .profile-image img {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                border: 3px solid #e5e7eb;
                object-fit: cover;
            }
            
            .profile-info {
                flex: 1;
            }
            
            .profile-info h2 {
                font-size: 24px;
                font-weight: 700;
                margin-bottom: 12px;
                color: #111827;
            }
            
            .info-grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 15px;
            }
            
            .info-item {
                display: flex;
                flex-direction: column;
                gap: 4px;
            }
            
            .info-item .label {
                font-size: 12px;
                color: #6b7280;
                font-weight: 600;
                text-transform: uppercase;
            }
            
            .info-item .value {
                font-size: 14px;
                color: #111827;
                font-weight: 500;
            }
            
            .info-item i {
                color: #0f2340;
                margin-right: 6px;
            }

            /* Buttons */
            .btn {
                padding: 8px 16px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 600;
                transition: all 0.3s;
                display: inline-flex;
                align-items: center;
                gap: 6px;
            }
            
            .btn-edit {
                background: #ffc107;
                color: #000;
                position: absolute;
                top: 20px;
                right: 20px;
            }
            
            .btn-edit:hover {
                background: #e0a800;
            }
            
            .btn-add {
                background: #28a745;
                color: white;
            }
            
            .btn-add:hover {
                background: #218838;
            }
            
            .btn-update {
                background: #007bff;
                color: white;
            }
            
            .btn-update:hover {
                background: #0056b3;
            }
            
            .btn-delete {
                background: #dc3545;
                color: white;
            }
            
            .btn-delete:hover {
                background: #c82333;
            }

            /* Section Header */
            .section-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            
            .section-header h3 {
                font-size: 20px;
                font-weight: 700;
                color: #111827;
            }

            /* Search Box */
            .search-form {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
            }
            
            .search-input {
                flex: 1;
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
            }
            
            .search-input:focus {
                outline: none;
                border-color: #0f2340;
            }
            
            .btn-search {
                background: #0f2340;
                color: white;
                padding: 8px 20px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 600;
            }
            
            .btn-search:hover {
                background: #0b1830;
            }

            /* Table */
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            }
            
            table thead th {
                background: #f8fafc;
                padding: 12px 16px;
                text-align: left;
                border-bottom: 1px solid #e5e7eb;
                font-weight: 600;
                color: #374151;
            }
            
            table tbody td {
                padding: 12px 16px;
                border-bottom: 1px solid #f1f5f9;
                color: #111827;
            }
            
            table tbody tr:hover {
                background: #f9fafb;
            }

            /* Modal */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                justify-content: center;
                align-items: center;
            }
            
            .modal-content {
                background: #fff;
                padding: 30px;
                border-radius: 12px;
                width: 800px;
                max-height: 90vh;
                overflow: auto;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            }
            
            .modal-content h2 {
                margin-bottom: 20px;
                font-size: 22px;
                font-weight: 700;
                color: #111827;
            }

            /* Form Grid */
            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px 20px;
            }
            
            .form-group {
                display: flex;
                flex-direction: column;
            }
            
            .form-group.full-width {
                grid-column: span 2;
            }
            
            .form-group label {
                font-weight: 600;
                margin-bottom: 6px;
                color: #374151;
                font-size: 14px;
            }
            
            .form-group input,
            .form-group select {
                padding: 8px 12px;
                border: 1px solid #d1d5db;
                border-radius: 6px;
                font-size: 14px;
                font-family: inherit;
            }
            
            .form-group input:focus,
            .form-group select:focus {
                outline: none;
                border-color: #0f2340;
            }
            
            .form-actions {
                margin-top: 20px;
                display: flex;
                gap: 10px;
                justify-content: flex-end;
                grid-column: span 2;
            }

            /* No data message */
            .no-data {
                text-align: center;
                padding: 40px;
                color: #6b7280;
                background: #fff;
                border-radius: 8px;
            }
            
            .no-data i {
                font-size: 48px;
                margin-bottom: 12px;
                opacity: 0.3;
            }

            @media (max-width: 768px) {
                .app {
                    flex-direction: column;
                }
                
                .sidebar {
                    width: 100%;
                    padding: 20px;
                }
                
                .info-grid {
                    grid-template-columns: 1fr;
                }
                
                .form-grid {
                    grid-template-columns: 1fr;
                }
                
                .modal-content {
                    width: 95%;
                }
            }
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar -->
            <div class="sidebar">
                <div class="brand">🚗 CAR MANAGEMENT</div>
                <nav class="nav">
                    <a href="#" class="active">
                        <i class="fas fa-user"></i>
                        <span>Profile</span>
                    </a>
                    <a href="#">
                        <i class="fas fa-car"></i>
                        <span>My Vehicles</span>
                    </a>
                    <a href="#">
                        <i class="fas fa-calendar-check"></i>
                        <span>Appointments</span>
                    </a>
                    <a href="#">
                        <i class="fas fa-history"></i>
                        <span>History</span>
                    </a>
                    <a href="#">
                        <i class="fas fa-cog"></i>
                        <span>Settings</span>
                    </a>
                    <a href="#" style="margin-top: auto;">
                        <i class="fas fa-sign-out-alt"></i>
                        <span>Logout</span>
                    </a>
                </nav>
            </div>

            <!-- Main Content -->
            <main class="main">
                <!-- Profile Card -->
                <div class="profile-card">
                    <% 
                        String userImage = user.getImage();
                        if (userImage == null || userImage.equals("null") || userImage.isEmpty()) {
                            userImage = "https://ui-avatars.com/api/?name=" + user.getFullName().replace(" ", "+") + "&size=100&background=0f2340&color=fff&bold=true";
                        }
                    %>
                    <div class="profile-image">
                        <img src="<%= userImage %>" alt="Profile" onerror="this.src='https://ui-avatars.com/api/?name=User&size=100&background=0f2340&color=fff&bold=true'">
                    </div>
                    <div class="profile-info">
                        <h2><%= user.getFullName() %></h2>
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-envelope"></i>Email
                                </div>
                                <div class="value"><%= user.getEmail() %></div>
                            </div>
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-phone"></i>Phone
                                </div>
                                <div class="value"><%= user.getPhone() != null ? user.getPhone() : "N/A" %></div>
                            </div>
                            <div class="info-item">
                                <div class="label">
                                    <i class="fas fa-venus-mars"></i>Gender
                                </div>
                                <div class="value"><%= user.getMale() ? "Male" : "Female" %></div>
                            </div>
                        </div>
                    </div>
                    <button class="btn btn-edit" onclick="document.getElementById('editProfileModal').style.display='flex'">
                        <i class="fas fa-edit"></i> Edit Profile
                    </button>
                </div>

                <!-- Cars Section -->
                <div class="section-header">
                    <h3><i class="fas fa-car"></i> My Vehicles</h3>
                    <button class="btn btn-add" onclick="document.getElementById('addModal').style.display='flex'">
                        <i class="fas fa-plus"></i> Add New Car
                    </button>
                </div>

                <!-- Search Form -->
                <form method="get" action="userProfileController" class="search-form">
                    <input type="text" name="search" class="search-input" value="<%= (search != null ? search : "") %>" placeholder="Search by brand, license plate...">
                    <button type="submit" class="btn-search">
                        <i class="fas fa-search"></i> Search
                    </button>
                </form>

                <!-- Cars Table -->
                <% if (cars != null && !cars.isEmpty()) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Brand</th>
                            <th>License Plate</th>
                            <th>Color</th>
                            <th>Model</th>
                            <th>Year</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Car c : cars) { %>
                        <tr>
                            <td><strong><%= c.getBrand() %></strong></td>
                            <td><%= c.getLicensePlate() %></td>
                            <td><%= c.getColor() %></td>
                            <td><%= c.getModel() != null ? c.getModel() : "N/A" %></td>
                            <td><%= c.getYear() != 0 ? c.getYear() : "N/A" %></td>
                            <td>
                                <button class="btn btn-update" onclick="document.getElementById('modal-<%= c.getCarId() %>').style.display='flex'">
                                    Update
                                </button>
                                <form action="userProfileController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="carId" value="<%= c.getCarId() %>">
                                    <button type="submit" class="btn btn-delete" onclick="return confirm('Are you sure you want to delete this car?')">
                                        Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="no-data">
                    <i class="fas fa-car"></i>
                    <p>No vehicles found. Add your first car!</p>
                </div>
                <% } %>
            </main>
        </div>

        <!-- Edit Profile Modal -->
        <div id="editProfileModal" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-user-edit"></i> Edit Profile</h2>
                <form action="userProfileController" method="post" class="form-grid">
                    <input type="hidden" name="action" value="updateProfile">
                    
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" name="fullName" value="<%= user.getFullName() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value="<%= user.getEmail() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Gender</label>
                        <select name="male">
                            <option value="true" <%= user.getMale() ? "selected" : "" %>>Male</option>
                            <option value="false" <%= !user.getMale() ? "selected" : "" %>>Female</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>Date of Birth</label>
                        <input type="date" name="dateOfBirth" value="<%= user.getDateOfBirth() != null ? user.getDateOfBirth() : "" %>">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-update">
                            <i class="fas fa-save"></i> Save Changes
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('editProfileModal').style.display='none'">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Add Car Modal -->
        <div id="addModal" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-plus-circle"></i> Add New Vehicle</h2>
                <form method="post" action="userProfileController" class="form-grid">
                    <input type="hidden" name="action" value="add">
                    
                    <div class="form-group">
                        <label>License Plate *</label>
                        <input name="licensePlate" required placeholder="e.g. 29A-12345">
                    </div>
                    
                    <div class="form-group">
                        <label>Brand *</label>
                        <input name="brand" required placeholder="e.g. Toyota">
                    </div>
                    
                    <div class="form-group">
                        <label>Model</label>
                        <input name="model" placeholder="e.g. Camry">
                    </div>
                    
                    <div class="form-group">
                        <label>Year</label>
                        <input type="number" name="year" placeholder="e.g. 2023">
                    </div>
                    
                    <div class="form-group">
                        <label>Color</label>
                        <input name="color" placeholder="e.g. White">
                    </div>
                    
                    <div class="form-group">
                        <label>Engine Number</label>
                        <input name="engineNumber" placeholder="Engine number">
                    </div>
                    
                    <div class="form-group">
                        <label>Chassis Number</label>
                        <input name="chassisNumber" placeholder="Chassis number">
                    </div>
                    
                    <div class="form-group">
                        <label>Purchase Date</label>
                        <input type="date" name="purchaseDate">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-add">
                            <i class="fas fa-check"></i> Add Vehicle
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('addModal').style.display='none'">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Car Modals -->
        <% if (cars != null) {
            for (Car c : cars) { %>
        <div id="modal-<%= c.getCarId() %>" class="modal">
            <div class="modal-content">
                <h2><i class="fas fa-edit"></i> Update Vehicle</h2>
                <form method="post" action="userProfileController" class="form-grid">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="carId" value="<%= c.getCarId() %>">
                    
                    <div class="form-group">
                        <label>License Plate *</label>
                        <input name="licensePlate" value="<%= c.getLicensePlate() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Brand *</label>
                        <input name="brand" value="<%= c.getBrand() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Model</label>
                        <input name="model" value="<%= c.getModel() != null ? c.getModel() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Year</label>
                        <input type="number" name="year" value="<%= c.getYear() != 0 ? c.getYear() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Color</label>
                        <input name="color" value="<%= c.getColor() %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Engine Number</label>
                        <input name="engineNumber" value="<%= c.getEngineNumber() != null ? c.getEngineNumber() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Chassis Number</label>
                        <input name="chassisNumber" value="<%= c.getChassisNumber() != null ? c.getChassisNumber() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label>Purchase Date</label>
                        <input type="date" name="purchaseDate" value="<%= c.getPurchaseDate() != null ? c.getPurchaseDate() : "" %>">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-update">
                            <i class="fas fa-save"></i> Save Changes
                        </button>
                        <button type="button" class="btn btn-delete" onclick="document.getElementById('modal-<%= c.getCarId() %>').style.display='none'">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <% } } %>

        <script>
            // Close modal when clicking outside
            window.onclick = function(event) {
                if (event.target.classList.contains('modal')) {
                    event.target.style.display = 'none';
                }
            }
        </script>
    </body>
</html>