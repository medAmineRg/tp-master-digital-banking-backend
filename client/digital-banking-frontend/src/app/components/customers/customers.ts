import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Customer } from '../../models/customer.model';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customers',
  imports: [FormsModule, CommonModule],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers: Customer[] = [];
  newCustomer: Customer = { name: '', email: '' };

  constructor(private readonly customerService: CustomerService) { }

  ngOnInit() {
    this.loadCustomers();
  }
  loadCustomers() {
    console.log('Loading customers...');
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        console.log('Customers received:', data);
        this.customers = data;
        console.log('Customers array length:', this.customers.length);
      },
      error: (error) => {
        console.error('Error loading customers:', error);
      }
    });
  }

  onSubmit() {
    if (this.newCustomer.name && this.newCustomer.email) {
      this.customerService.createCustomer(this.newCustomer).subscribe({
        next: (customer) => {
          this.customers.push(customer);
          this.newCustomer = { name: '', email: '' };
        },
        error: (error) => {
          console.error('Error creating customer:', error);
        }
      });
    }
  }
  deleteCustomer(id: number) {
    this.customerService.deleteCustomer(id).subscribe({
      next: () => {
        this.customers = this.customers.filter(c => c.id !== id);
      },
      error: (error) => {
        console.error('Error deleting customer:', error);
      }
    });
  }

  getAccountTypeDisplay(type: string): string {
    switch (type) {
      case 'CA':
        return 'Current Account';
      case 'SA':
        return 'Savings Account';
      default:
        return type;
    }
  }
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  trackByCustomerId(index: number, customer: Customer): number {
    return customer.id || index;
  }

  trackByAccountId(index: number, account: any): string {
    return account.id || index;
  }
}
