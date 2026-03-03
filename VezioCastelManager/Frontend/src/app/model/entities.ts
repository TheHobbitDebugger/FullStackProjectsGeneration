export interface Visitor {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  ssn: string;
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  username: string;
}

export interface Ticket {
  id: number;
  date: string;
  price: number;
  sellerId: number;
  visitorId: number;
  seller?: User;
  visitor?: Visitor;
}

export interface Login {
  username: string;
  password: string;
}

export interface Token {
  token: string;
}
