export interface BankAccount {
    id: string;
    balance: number;
    createdAt: string;
    status: string;
    type: string;
    customer?: any;
    accountOperations?: any;
}

export interface Customer {
    id?: number;
    name: string;
    email: string;
    bankAccounts?: BankAccount[];
}
