import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccount, CurrentAccount, SavingAccount, CreateAccountRequest } from '../models/account.model';

@Injectable({
    providedIn: 'root'
})
export class AccountService {
    private baseUrl = 'http://localhost:8082/api/accounts';

    constructor(private http: HttpClient) { }

    createCurrentAccount(request: CreateAccountRequest): Observable<CurrentAccount> {
        return this.http.post<CurrentAccount>(`${this.baseUrl}/current`, request);
    }

    createSavingAccount(request: CreateAccountRequest): Observable<SavingAccount> {
        return this.http.post<SavingAccount>(`${this.baseUrl}/saving`, request);
    }

    getAllAccounts(): Observable<BankAccount[]> {
        return this.http.get<BankAccount[]>(this.baseUrl);
    }

    getAccountById(id: string): Observable<BankAccount> {
        return this.http.get<BankAccount>(`${this.baseUrl}/${id}`);
    }

    getAccountsByCustomerId(customerId: number): Observable<BankAccount[]> {
        return this.http.get<BankAccount[]>(`${this.baseUrl}/customer/${customerId}`);
    }

    deleteAccount(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
