import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';

@Injectable({
    providedIn: 'root'
})
export class CustomerService {
    private baseUrl = 'http://localhost:8082/api/customers';

    constructor(private http: HttpClient) { }

    createCustomer(customer: Customer): Observable<Customer> {
        return this.http.post<Customer>(this.baseUrl, customer);
    }

    getAllCustomers(): Observable<Customer[]> {
        return this.http.get<Customer[]>(this.baseUrl);
    }

    getCustomerById(id: number): Observable<Customer> {
        return this.http.get<Customer>(`${this.baseUrl}/${id}`);
    }

    updateCustomer(customer: Customer): Observable<Customer> {
        return this.http.put<Customer>(this.baseUrl, customer);
    }

    deleteCustomer(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    searchCustomersByName(name: string): Observable<Customer[]> {
        return this.http.get<Customer[]>(`${this.baseUrl}/search?name=${name}`);
    }
}
