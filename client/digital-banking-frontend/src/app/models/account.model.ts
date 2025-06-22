export interface BankAccount {
    id?: string;
    balance: number;
    currency: string;
    status: string;
    createdAt?: string;
    customerDTO?: {
        id: number;
        name: string;
        email: string;
    };
}

export interface CurrentAccount extends BankAccount {
    overDraft: number;
}

export interface SavingAccount extends BankAccount {
    interestRate: number;
}

export interface CreateAccountRequest {
    balance: number;
    customerDTO: number;
    overdraft?: number;
    interestRate?: number;
}
