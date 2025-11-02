const API_URL = 'http://localhost:8080/api/admin';

// Kiểm tra đăng nhập
function checkAuth() {
    const employee = JSON.parse(sessionStorage.getItem('employee'));
    if (!employee) {
        window.location.href = '/login.html';
        return null;
    }
    return employee;
}

// Kiểm tra quyền ADMIN
function checkAdminRole() {
    const employee = checkAuth();
    if (employee && employee.role !== 'ADMIN') {
        alert('Bạn không có quyền truy cập trang này!');
        window.location.href = '/admin/customers.html';
        return false;
    }
    return true;
}

// Hiển thị thông tin user trong header
function displayUserInfo() {
    const employee = checkAuth();
    if (!employee) return;
    
    document.getElementById('userName').textContent = employee.name;
    document.getElementById('userRole').textContent = 
        employee.role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên';
}

// Đăng xuất
function logout() {
    sessionStorage.removeItem('employee');
    window.location.href = '/login.html';
}

// Format date
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

// Format datetime
function formatDateTime(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
}

// Show loading
function showLoading() {
    document.querySelector('.loading').style.display = 'block';
}

// Hide loading
function hideLoading() {
    document.querySelector('.loading').style.display = 'none';
}

// Show toast notification
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `alert alert-${type} position-fixed top-0 end-0 m-3`;
    toast.style.zIndex = '9999';
    toast.textContent = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

// Confirm delete
function confirmDelete(message = 'Bạn có chắc chắn muốn xóa?') {
    return confirm(message);
}

// Get status badge class
function getStatusBadgeClass(status) {
    return status === 'ACTIVE' ? 'bg-success' : 'bg-danger';
}

// Get status text
function getStatusText(status) {
    return status === 'ACTIVE' ? 'Hoạt động' : 'Ngừng hoạt động';
}

// Get gender text
function getGenderText(gender) {
    switch(gender) {
        case 'MALE': return 'Nam';
        case 'FEMALE': return 'Nữ';
        case 'OTHER': return 'Khác';
        default: return '';
    }
}

// Get role text
function getRoleText(role) {
    return role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên';
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    displayUserInfo();
});
