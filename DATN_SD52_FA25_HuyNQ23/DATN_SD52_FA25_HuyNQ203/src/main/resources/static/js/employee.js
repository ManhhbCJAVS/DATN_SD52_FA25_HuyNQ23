let currentPage = 0;
const pageSize = 10;
let currentKeyword = '';
let currentStatus = '';
let currentRole = '';

// Check auth - Only ADMIN can access
document.addEventListener('DOMContentLoaded', () => {
    if (!checkAdminRole()) return;
    loadEmployees();
});

// Load employees
async function loadEmployees(page = 0) {
    showLoading();
    currentPage = page;
    
    try {
        const url = `${API_URL}/employees?page=${page}&size=${pageSize}&keyword=${currentKeyword}&status=${currentStatus}&role=${currentRole}`;
        const response = await fetch(url);
        
        if (response.ok) {
            const data = await response.json();
            displayEmployees(data.employees);
            displayPagination(data);
        } else {
            showToast('Không thể tải danh sách nhân viên', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showToast('Có lỗi xảy ra khi tải dữ liệu', 'danger');
    } finally {
        hideLoading();
    }
}

// Display employees
function displayEmployees(employees) {
    const tbody = document.getElementById('employeeTableBody');
    tbody.innerHTML = '';
    
    if (employees.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    employees.forEach(employee => {
        const row = `
            <tr>
                <td>${employee.code}</td>
                <td>${employee.name}</td>
                <td>${employee.phone || ''}</td>
                <td>${employee.email || ''}</td>
                <td><span class="badge ${employee.role === 'ADMIN' ? 'bg-primary' : 'bg-secondary'}">${getRoleText(employee.role)}</span></td>
                <td>${getGenderText(employee.gender)}</td>
                <td><span class="badge ${getStatusBadgeClass(employee.status)}">${getStatusText(employee.status)}</span></td>
                <td>
                    <div class="btn-group-action">
                        <button class="btn btn-sm btn-info" onclick="viewEmployee(${employee.id})" title="Xem chi tiết">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="editEmployee(${employee.id})" title="Sửa">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteEmployee(${employee.id})" title="Xóa">
                            <i class="fas fa-trash"></i>
                        </button>
                        <button class="btn btn-sm ${employee.status === 'ACTIVE' ? 'btn-secondary' : 'btn-success'}" 
                                onclick="toggleStatus(${employee.id}, '${employee.status}')" 
                                title="${employee.status === 'ACTIVE' ? 'Khóa' : 'Mở khóa'}">
                            <i class="fas fa-${employee.status === 'ACTIVE' ? 'lock' : 'unlock'}"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}

// Display pagination
function displayPagination(data) {
    const { currentPage, totalPages, totalItems } = data;
    
    // Page info
    const start = currentPage * pageSize + 1;
    const end = Math.min((currentPage + 1) * pageSize, totalItems);
    document.getElementById('pageInfo').textContent = `${start}-${end} / ${totalItems}`;
    
    // Pagination buttons
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';
    
    // Previous button
    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 0 ? 'disabled' : ''}`;
    prevLi.innerHTML = `<a class="page-link" href="#" onclick="loadEmployees(${currentPage - 1})">Trước</a>`;
    pagination.appendChild(prevLi);
    
    // Page numbers
    for (let i = 0; i < totalPages; i++) {
        if (i === 0 || i === totalPages - 1 || (i >= currentPage - 2 && i <= currentPage + 2)) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" onclick="loadEmployees(${i})">${i + 1}</a>`;
            pagination.appendChild(li);
        } else if (i === currentPage - 3 || i === currentPage + 3) {
            const li = document.createElement('li');
            li.className = 'page-item disabled';
            li.innerHTML = '<a class="page-link">...</a>';
            pagination.appendChild(li);
        }
    }
    
    // Next button
    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}`;
    nextLi.innerHTML = `<a class="page-link" href="#" onclick="loadEmployees(${currentPage + 1})">Sau</a>`;
    pagination.appendChild(nextLi);
}

// Search employees
function searchEmployees() {
    currentKeyword = document.getElementById('searchKeyword').value;
    currentStatus = document.getElementById('filterStatus').value;
    currentRole = document.getElementById('filterRole').value;
    loadEmployees(0);
}

// Open add modal
function openAddModal() {
    document.getElementById('modalTitle').textContent = 'Thêm nhân viên';
    document.getElementById('employeeForm').reset();
    document.getElementById('employeeId').value = '';
    document.getElementById('password').required = true;
    document.getElementById('email').required = true;
    new bootstrap.Modal(document.getElementById('employeeModal')).show();
}

// View employee
async function viewEmployee(id) {
    try {
        const response = await fetch(`${API_URL}/employees/${id}`);
        if (response.ok) {
            const employee = await response.json();
            alert(`Thông tin nhân viên:\n\nMã: ${employee.code}\nTên: ${employee.name}\nSĐT: ${employee.phone || ''}\nEmail: ${employee.email || ''}\nVai trò: ${getRoleText(employee.role)}\nGiới tính: ${getGenderText(employee.gender)}\nNgày sinh: ${formatDate(employee.birthday)}\nGhi chú: ${employee.note || ''}\nTrạng thái: ${getStatusText(employee.status)}`);
        }
    } catch (error) {
        showToast('Không thể tải thông tin nhân viên', 'danger');
    }
}

// Edit employee
async function editEmployee(id) {
    try {
        const response = await fetch(`${API_URL}/employees/${id}`);
        if (response.ok) {
            const employee = await response.json();
            
            document.getElementById('modalTitle').textContent = 'Sửa thông tin nhân viên';
            document.getElementById('employeeId').value = employee.id;
            document.getElementById('name').value = employee.name;
            document.getElementById('phone').value = employee.phone || '';
            document.getElementById('email').value = employee.email || '';
            document.getElementById('role').value = employee.role || '';
            document.getElementById('birthday').value = employee.birthday || '';
            document.getElementById('gender').value = employee.gender || '';
            document.getElementById('status').value = employee.status;
            document.getElementById('note').value = employee.note || '';
            document.getElementById('password').value = '';
            document.getElementById('password').required = false;
            document.getElementById('email').required = true;
            
            new bootstrap.Modal(document.getElementById('employeeModal')).show();
        }
    } catch (error) {
        showToast('Không thể tải thông tin nhân viên', 'danger');
    }
}

// Save employee
async function saveEmployee() {
    const id = document.getElementById('employeeId').value;
    const data = {
        name: document.getElementById('name').value,
        phone: document.getElementById('phone').value || null,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value || null,
        role: document.getElementById('role').value,
        birthday: document.getElementById('birthday').value || null,
        gender: document.getElementById('gender').value || null,
        status: document.getElementById('status').value,
        note: document.getElementById('note').value || null
    };
    
    try {
        const url = id ? `${API_URL}/employees/${id}` : `${API_URL}/employees`;
        const method = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            showToast(`${id ? 'Cập nhật' : 'Thêm'} nhân viên thành công`, 'success');
            bootstrap.Modal.getInstance(document.getElementById('employeeModal')).hide();
            loadEmployees(currentPage);
        } else {
            const error = await response.json();
            showToast(error.message || 'Có lỗi xảy ra', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showToast('Có lỗi xảy ra khi lưu dữ liệu', 'danger');
    }
}

// Delete employee
async function deleteEmployee(id) {
    if (!confirmDelete('Bạn có chắc chắn muốn xóa nhân viên này?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/employees/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Xóa nhân viên thành công', 'success');
            loadEmployees(currentPage);
        } else {
            showToast('Không thể xóa nhân viên', 'danger');
        }
    } catch (error) {
        showToast('Có lỗi xảy ra khi xóa', 'danger');
    }
}

// Toggle status
async function toggleStatus(id, currentStatus) {
    const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    
    try {
        const response = await fetch(`${API_URL}/employees/${id}/status?status=${newStatus}`, {
            method: 'PATCH'
        });
        
        if (response.ok) {
            showToast('Cập nhật trạng thái thành công', 'success');
            loadEmployees(currentPage);
        } else {
            showToast('Không thể cập nhật trạng thái', 'danger');
        }
    } catch (error) {
        showToast('Có lỗi xảy ra', 'danger');
    }
}
