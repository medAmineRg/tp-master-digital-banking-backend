import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BankAccount, CreateAccountRequest } from '../../models/account.model';
import { Customer } from '../../models/customer.model';
import { AccountService } from '../../services/account.service';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-accounts',
  imports: [FormsModule, CommonModule],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements OnInit {
  accounts: BankAccount[] = [];
  customers: Customer[] = [];
  newAccount: CreateAccountRequest = {
    balance: 0,
    customerDTO: 0
  };
  accountType: string = '';

  constructor(
    private accountService: AccountService,
    private customerService: CustomerService
  ) { }

  ngOnInit() {
    this.loadAccounts();
    this.loadCustomers();
  }

  loadAccounts() {
    this.accountService.getAllAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: (error) => {
        console.error('Error loading accounts:', error);
      }
    });
  }

  loadCustomers() {
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
      },
      error: (error) => {
        console.error('Error loading customers:', error);
      }
    });
  }

  onSubmit() {
    if (this.newAccount.balance && this.newAccount.customerDTO && this.accountType) {
      if (this.accountType === 'current' && this.newAccount.overdraft !== undefined) {
        this.accountService.createCurrentAccount(this.newAccount).subscribe({
          next: (account) => {
            this.accounts.push(account);
            this.resetForm();
          },
          error: (error) => {
            console.error('Error creating current account:', error);
          }
        });
      } else if (this.accountType === 'saving' && this.newAccount.interestRate !== undefined) {
        this.accountService.createSavingAccount(this.newAccount).subscribe({
          next: (account) => {
            this.accounts.push(account);
            this.resetForm();
          },
          error: (error) => {
            console.error('Error creating saving account:', error);
          }
        });
      }
    }
  }

  deleteAccount(id: string) {
    this.accountService.deleteAccount(id).subscribe({
      next: () => {
        this.accounts = this.accounts.filter(a => a.id !== id);
      },
      error: (error) => {
        console.error('Error deleting account:', error);
      }
    });
  }

  private resetForm() {
    this.newAccount = {
      balance: 0,
      customerDTO: 0
    };
    this.accountType = '';
  }
}
