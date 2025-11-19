let currentPage = 0;
const pageSize = 10;
let currentKeyword = '';
let currentStatus = '';

// Check auth and show employee menu for admin
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    const employee = JSON.parse(sessionStorage.getItem('employee'));
    if (employee && employee.role === 'ADMIN') {
        document.getElementById('employeeMenu').style.display = 'block';
    }
    loadCustomers();
});

// Load customers
async function loadCustomers(page = 0) {
    showLoading();
    currentPage = page;
    
    try {
        const url = `${API_URL}/customers?page=${page}&size=${pageSize}&keyword=${currentKeyword}&status=${currentStatus}`;
        const response = await fetch(url);
        
        if (response.ok) {
            const data = await response.json();
            displayCustomers(data.customers);
            displayPagination(data);
        } else {
            showToast('Không thể tải danh sách khách hàng', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showToast('Có lỗi xảy ra khi tải dữ liệu', 'danger');
    } finally {
        hideLoading();
    }
}

// Display customers
function displayCustomers(customers) {
    const tbody = document.getElementById('customerTableBody');
    tbody.innerHTML = '';
    
    if (customers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    customers.forEach(customer => {
        const row = `
            <tr>
                <td>${customer.code}</td>
                <td>${customer.name}</td>
                <td>${customer.phone || ''}</td>
                <td>${customer.email || ''}</td>
                <td>${getGenderText(customer.gender)}</td>
                <td>${formatDate(customer.birthday)}</td>
                <td><span class="badge ${getStatusBadgeClass(customer.status)}">${getStatusText(customer.status)}</span></td>
                <td>
                    <div class="btn-group-action">
                        <button class="btn btn-sm btn-info" onclick="viewCustomer(${customer.id})" title="Xem chi tiết">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="editCustomer(${customer.id})" title="Sửa">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteCustomer(${customer.id})" title="Xóa">
                            <i class="fas fa-trash"></i>
                        </button>
                        <button class="btn btn-sm ${customer.status === 'ACTIVE' ? 'btn-secondary' : 'btn-success'}" 
                                onclick="toggleStatus(${customer.id}, '${customer.status}')" 
                                title="${customer.status === 'ACTIVE' ? 'Khóa' : 'Mở khóa'}">
                            <i class="fas fa-${customer.status === 'ACTIVE' ? 'lock' : 'unlock'}"></i>
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
    prevLi.innerHTML = `<a class="page-link" href="#" onclick="loadCustomers(${currentPage - 1})">Trước</a>`;
    pagination.appendChild(prevLi);
    
    // Page numbers
    for (let i = 0; i < totalPages; i++) {
        if (i === 0 || i === totalPages - 1 || (i >= currentPage - 2 && i <= currentPage + 2)) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" onclick="loadCustomers(${i})">${i + 1}</a>`;
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
    nextLi.innerHTML = `<a class="page-link" href="#" onclick="loadCustomers(${currentPage + 1})">Sau</a>`;
    pagination.appendChild(nextLi);
}

// Search customers
function searchCustomers() {
    currentKeyword = document.getElementById('searchKeyword').value;
    currentStatus = document.getElementById('filterStatus').value;
    loadCustomers(0);
}

// Open add modal
function openAddModal() {
    document.getElementById('modalTitle').textContent = 'Thêm khách hàng';
    document.getElementById('customerForm').reset();
    document.getElementById('customerId').value = '';
    document.getElementById('password').required = true;
    new bootstrap.Modal(document.getElementById('customerModal')).show();
}

// View customer
async function viewCustomer(id) {
    try {
        const response = await fetch(`${API_URL}/customers/${id}`);
        if (response.ok) {
            const customer = await response.json();
            alert(`Thông tin khách hàng:\n\nMã: ${customer.code}\nTên: ${customer.name}\nSĐT: ${customer.phone || ''}\nEmail: ${customer.email || ''}\nGiới tính: ${getGenderText(customer.gender)}\nNgày sinh: ${formatDate(customer.birthday)}\nTrạng thái: ${getStatusText(customer.status)}`);
        }
    } catch (error) {
        showToast('Không thể tải thông tin khách hàng', 'danger');
    }
}

// Edit customer
async function editCustomer(id) {
    try {
        const response = await fetch(`${API_URL}/customers/${id}`);
        if (response.ok) {
            const customer = await response.json();
            
            document.getElementById('modalTitle').textContent = 'Sửa thông tin khách hàng';
            document.getElementById('customerId').value = customer.id;
            document.getElementById('name').value = customer.name;
            document.getElementById('phone').value = customer.phone || '';
            document.getElementById('email').value = customer.email || '';
            document.getElementById('birthday').value = customer.birthday || '';
            document.getElementById('gender').value = customer.gender || '';
            document.getElementById('status').value = customer.status;
            document.getElementById('password').value = '';
            document.getElementById('password').required = false;
            
            new bootstrap.Modal(document.getElementById('customerModal')).show();
        }
    } catch (error) {
        showToast('Không thể tải thông tin khách hàng', 'danger');
    }
}

// Save customer
async function saveCustomer() {
    const id = document.getElementById('customerId').value;
    const data = {
        name: document.getElementById('name').value,
        phone: document.getElementById('phone').value || null,
        email: document.getElementById('email').value || null,
        password: document.getElementById('password').value || null,
        birthday: document.getElementById('birthday').value || null,
        gender: document.getElementById('gender').value || null,
        status: document.getElementById('status').value
    };
    
    try {
        const url = id ? `${API_URL}/customers/${id}` : `${API_URL}/customers`;
        const method = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            showToast(`${id ? 'Cập nhật' : 'Thêm'} khách hàng thành công`, 'success');
            bootstrap.Modal.getInstance(document.getElementById('customerModal')).hide();
            loadCustomers(currentPage);
        } else {
            const error = await response.json();
            showToast(error.message || 'Có lỗi xảy ra', 'danger');
        }
    } catch (error) {
        console.error('Error:', error);
        showToast('Có lỗi xảy ra khi lưu dữ liệu', 'danger');
    }
}

// Delete customer
async function deleteCustomer(id) {
    if (!confirmDelete('Bạn có chắc chắn muốn xóa khách hàng này?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/customers/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Xóa khách hàng thành công', 'success');
            loadCustomers(currentPage);
        } else {
            showToast('Không thể xóa khách hàng', 'danger');
        }
    } catch (error) {
        showToast('Có lỗi xảy ra khi xóa', 'danger');
    }
}

// Toggle status
async function toggleStatus(id, currentStatus) {
    const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    
    try {
        const response = await fetch(`${API_URL}/customers/${id}/status?status=${newStatus}`, {
            method: 'PATCH'
        });
        
        if (response.ok) {
            showToast('Cập nhật trạng thái thành công', 'success');
            loadCustomers(currentPage);
        } else {
            showToast('Không thể cập nhật trạng thái', 'danger');
        }
    } catch (error) {
        showToast('Có lỗi xảy ra', 'danger');
    }
}
