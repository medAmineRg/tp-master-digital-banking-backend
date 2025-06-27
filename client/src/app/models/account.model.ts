export interface BankAccount {
    id?: string;
    balance: number;
    currency?: string;
    status: string;
    createdAt?: string;
    type?: string;
    customer?: {
        id: number;
        name: string;
        email: string;
        bankAccounts?: any;
    };
    accountOperations?: any;
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
