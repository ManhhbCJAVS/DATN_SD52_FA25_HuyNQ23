const API_URL = 'http://localhost:8080/api/admin';

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');
    
    try {
        const response = await fetch(`${API_URL}/employees/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            const employee = await response.json();
            
            // Lưu thông tin đăng nhập
            sessionStorage.setItem('employee', JSON.stringify(employee));
            
            // Chuyển hướng dựa trên role
            if (employee.role === 'ADMIN') {
                window.location.href = '/admin/employees.html';
            } else {
                window.location.href = '/admin/customers.html';
            }
        } else {
            const error = await response.json();
            errorMessage.textContent = error.message || 'Email hoặc mật khẩu không đúng';
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        errorMessage.textContent = 'Có lỗi xảy ra. Vui lòng thử lại!';
        errorMessage.style.display = 'block';
    }
});
