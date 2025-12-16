// API service for Spring Boot backend
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

class ApiService {
  private baseURL: string;
  private token: string | null = null;

  constructor() {
    this.baseURL = API_BASE_URL;
    this.token = localStorage.getItem('token');
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseURL}${endpoint}`;
    
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...(options.headers as Record<string, string>),
    };

    if (this.token) {
      headers.Authorization = `Bearer ${this.token}`;
    }

    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      // Try to extract a meaningful error message from the response body.
      let errorMessage = `HTTP error! status: ${response.status}`;
      try {
        const errorData = await response.json();
        if (errorData) {
          if (typeof errorData === 'string') {
            errorMessage = errorData;
          } else if (errorData.message) {
            errorMessage = errorData.message;
          } else if (errorData.error) {
            errorMessage = errorData.error;
          } else if (Array.isArray(errorData)) {
            errorMessage = errorData.join(' | ');
          } else if (typeof errorData === 'object') {
            // Flatten values into a readable string (useful for validation errors)
            const vals: string[] = Object.values(errorData)
              .flatMap((v: any) => (Array.isArray(v) ? v : [v]))
              .map((v: any) => (typeof v === 'string' ? v : JSON.stringify(v)));
            if (vals.length > 0) errorMessage = vals.join(' | ');
            else errorMessage = JSON.stringify(errorData);
          }
        }
      } catch (e) {
        // ignore JSON parse errors and keep generic message
      }
      console.error('API request error', response.status, errorMessage);
      throw new Error(errorMessage);
    }

    // Check if there's content before trying to parse JSON
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      const text = await response.text();
      return text ? JSON.parse(text) : null;
    }
    
    return null;
  }

  // Auth methods
  async login(email: string, password: string, userType: string) {
    if (userType === 'company') {
      const response = await this.request<{
        id: number;
        companyName: string;
        cnpj: string;
        email: string;
      }>('/company/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      });

      return {
        token: 'dummy-token', // Backend não implementa JWT ainda
        userId: response.id.toString(),
        email: response.email,
        userType: 'company'
      };
    }
    
    throw new Error('Login disponível apenas para empresas no momento');
  }

  async register(data: {
    email: string;
    password: string;
    userType: string;
    name?: string;
    cpf?: string;
    rg?: string;
    address?: string;
    institutionId?: string;
    course?: string;
    department?: string;
    companyName?: string;
    cnpj?: string;
  }) {
    if (data.userType === 'company') {
      const response = await this.request<{
        id: number;
        companyName: string;
        cnpj: string;
        email: string;
      }>('/company/register', {
        method: 'POST',
        body: JSON.stringify({
          companyName: data.companyName,
          cnpj: data.cnpj,
          email: data.email,
          password: data.password
        }),
      });

      return {
        token: 'dummy-token',
        userId: response.id.toString(),
        email: response.email,
        userType: 'company'
      };
    }
    
    throw new Error('Cadastro disponível apenas para empresas no momento');
  }

  logout() {
    this.token = null;
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('userType');
  }

  // Institution methods
  async getInstitutions() {
    return this.request<Array<{
      id: string;
      name: string;
      createdAt: string;
    }>>('/institutions');
  }

  // Professor methods
  async sendCoins(professorId: string | null, studentId: string, amount: number, reason: string) {
    const body: any = { studentId, amount, reason };
    if (professorId) body.professorId = professorId;
    return this.request<{
      id: string;
      professorId: string;
      studentId: string;
      amount: number;
      reason: string;
      createdAt: string;
    }>('/professor/send-coins', {
      method: 'POST',
      body: JSON.stringify(body),
    });
  }

  async getSentTransactions(professorId?: string) {
    const qs = professorId ? `?professorId=${professorId}` : '';
    return this.request<Array<{
      id: string;
      professorId: string;
      studentId: string;
      amount: number;
      reason: string;
      createdAt: string;
      student?: any;
    }>>(`/professor/transactions${qs}`);
  }

  async getStudentsByInstitution(institutionId: string) {
    return this.request<Array<{
      id: string;
      name: string;
      cpf: string;
      course: string;
      coinBalance: number;
    }>>(`/professor/students/${institutionId}`);
  }

  async getStudentById(id: string) {
    return this.request<{
      id: number;
      name: string;
      email: string;
      cpf: string;
      rg: string;
      address: string;
      institutionId: number;
      institutionName?: string;
      course: string;
      coinBalance: number;
    }>(`/students/${id}`);
  }

  async getStudentTransactions(studentId: string) {
    return this.request<Array<any>>(`/students/${studentId}/transactions`);
  }

  async getStudentRedemptions(studentId: string) {
    return this.request<Array<any>>(`/students/${studentId}/redemptions`);
  }

  async searchStudents(institutionId: string, name: string) {
    return this.request<Array<{
      id: string;
      name: string;
      cpf: string;
      course: string;
      coinBalance: number;
    }>>(`/professor/students/search?institutionId=${institutionId}&name=${name}`);
  }

  // Professor auth methods
  async professorLogin(email: string, password: string) {
    return this.request<{ 
      id: number;
      name: string;
      email: string;
      cpf: string;
      department: string;
      institutionId: number;
      institutionName: string;
      coinBalance: number;
      message?: string;
    }>(
      '/professors/login',
      {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      }
    );
  }

  async professorRegister(data: {
    name: string;
    email: string;
    cpf: string;
    institutionId: number;
    department: string;
    password: string;
  }) {
    return this.request<{
      id: number;
      name: string;
      email: string;
      cpf: string;
      department: string;
      institutionId: number;
      institutionName: string;
      coinBalance: number;
      message?: string;
    }>(
      '/professors/register',
      {
        method: 'POST',
        body: JSON.stringify(data),
      }
    );
  }

  // Student methods
  async redeemAdvantage(advantageId: string) {
    return this.request<{
      id: string;
      studentId: string;
      advantageId: string;
      couponCode: string;
      createdAt: string;
    }>('/student/redeem', {
      method: 'POST',
      body: JSON.stringify({ advantageId }),
    });
  }

  async getReceivedTransactions() {
    return this.request<Array<{
      id: string;
      professorId: string;
      studentId: string;
      amount: number;
      reason: string;
      createdAt: string;
    }>>('/student/transactions');
  }

  async getRedemptions() {
    return this.request<Array<{
      id: string;
      studentId: string;
      advantageId: string;
      couponCode: string;
      createdAt: string;
    }>>('/student/redemptions');
  }

  // Company methods
  async getCompanyById(id: string) {
    return this.request<{
      id: string;
      companyName: string;
      cnpj: string;
      email: string;
    }>(`/company/${id}`);
  }

  // Simple company auth without localStorage side-effects
  async companyLogin(email: string, password: string) {
    return this.request<{ id: string; companyName: string; cnpj: string; email: string; message?: string }>(
      '/company/login',
      {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      }
    );
  }

  async companyRegister(data: { email: string; password: string; companyName: string; cnpj: string }) {
    return this.request<{ id: string; companyName: string; cnpj: string; email: string; message?: string }>(
      '/company/register',
      {
        method: 'POST',
        body: JSON.stringify(data),
      }
    );
  }

  // Student auth methods
  async studentLogin(email: string, password: string) {
    return this.request<{ 
      id: number; 
      name: string; 
      email: string; 
      cpf: string;
      rg: string;
      address: string;
      institutionId: number;
      institutionName: string;
      course: string;
      coinBalance: number;
      message?: string 
    }>(
      '/students/login',
      {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      }
    );
  }

  async studentRegister(data: { 
    name: string;
    email: string; 
    cpf: string;
    rg: string;
    address: string;
    institutionId: number;
    course: string;
    password: string;
  }) {
    return this.request<{ 
      id: number; 
      name: string; 
      email: string; 
      cpf: string;
      rg: string;
      address: string;
      institutionId: number;
      institutionName: string;
      course: string;
      coinBalance: number;
      message?: string 
    }>(
      '/students/register',
      {
        method: 'POST',
        body: JSON.stringify(data),
      }
    );
  }

  async getCompanyAdvantages(companyId: string) {
    return this.request<Array<{
      id: string;
      company: any;
      title: string;
      description: string;
      photoUrl?: string;
      coinCost: number;
      isActive: boolean;
      maxRedemptions: number;
      currentRedemptions: number;
      createdAt: string;
    }>>(`/advantages/company/${companyId}`);
  }

  async createAdvantageForCompany(companyId: string, data: {
    title: string;
    description: string;
    photoUrl?: string;
    coinCost: number;
    isActive?: boolean;
    maxRedemptions?: number;
  }) {
    return this.request(`/advantages/company/${companyId}`, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateAdvantageV2(id: string, data: {
    title: string;
    description: string;
    photoUrl?: string;
    coinCost: number;
    isActive?: boolean;
    maxRedemptions?: number;
  }) {
    return this.request(`/advantages/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteAdvantageV2(id: string) {
    return this.request(`/advantages/${id}`, {
      method: 'DELETE',
    });
  }

  async getRedemptionsByCompany(companyId: string) {
    return this.request<Array<{
      id: string;
      couponCode: string;
      studentEmail: string;
      studentName: string;
      createdAt: string;
    }>>(`/advantages/redemptions/company/${companyId}`);
  }
  async createAdvantage(data: {
    title: string;
    description: string;
    photoUrl?: string;
    coinCost: number;
    isActive?: boolean;
    maxRedemptions?: number;
  }) {
    return this.request<{
      id: string;
      companyId: string;
      title: string;
      description: string;
      photoUrl?: string;
      coinCost: number;
      isActive: boolean;
      maxRedemptions: number;
      currentRedemptions: number;
      createdAt: string;
    }>('/company/advantages', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }



  async deleteAdvantage(id: string) {
    return this.request(`/company/advantages/${id}`, {
      method: 'DELETE',
    });
  }

  async getCompanyRedemptions() {
    return this.request<Array<{
      id: string;
      studentId: string;
      advantageId: string;
      couponCode: string;
      createdAt: string;
    }>>('/company/redemptions');
  }

  // Public advantage methods
  async getAvailableAdvantages() {
    return this.request<Array<{
      id: string;
      company?: any;
      title: string;
      description: string;
      photoUrl?: string;
      coinCost: number;
      isActive: boolean;
      maxRedemptions: number;
      currentRedemptions: number;
      createdAt: string;
    }>>('/advantages');
  }

  /* async getAffordableAdvantages(maxCost: number) {
    return this.request<Array<{
      id: string;
      companyId: string;
      title: string;
      description: string;
      photoUrl?: string;
      coinCost: number;
      isActive: boolean;
      maxRedemptions: number;
      currentRedemptions: number;
      createdAt: string;
    }>>(`/advantages/affordable/${maxCost}`);
  } */

  /* async getAdvantageById(id: string) {
    return this.request<{
      id: string;
      company?: any;
      title: string;
      description: string;
      photoUrl?: string;
      coinCost: number;
      isActive: boolean;
      maxRedemptions: number;
      currentRedemptions: number;
      createdAt: string;
    }>(`/advantages/${id}`);
  } */

  // Student redeem without auth, using provided identity fields
  async redeemAdvantageByStudent(params: { advantageId: number; studentEmail: string; studentName: string }) {
    return this.request('/advantages/redeem', {
      method: 'POST',
      body: JSON.stringify(params),
    });
  }
}

export const apiService = new ApiService();
