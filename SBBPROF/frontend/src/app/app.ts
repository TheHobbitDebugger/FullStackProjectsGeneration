import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TopMenu } from "./top-menu/top-menu";
import { BookRoom } from "./book-room/book-room";
import { LoginTest } from "./login-test/login-test";
import { FormLogin } from "./form-login/form-login";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopMenu, FormLogin],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('superiorbb');
}
