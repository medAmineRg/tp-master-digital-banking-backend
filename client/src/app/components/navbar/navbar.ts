import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  searchQuery: string = '';

  constructor(
    private router: Router,
    private customerService: CustomerService
  ) { }

  searchCustomers() {
    if (this.searchQuery.trim()) {
      // Navigate to customers page with the search query
      this.router.navigate(['/customers'], {
        queryParams: { search: this.searchQuery }
      });
    }
  }
}
