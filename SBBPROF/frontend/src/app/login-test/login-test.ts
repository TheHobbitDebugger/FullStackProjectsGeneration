import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';

@Component({
  selector: 'app-login-test',
  imports: [],
  templateUrl: './login-test.html',
  styleUrl: './login-test.css',
})
export class LoginTest {


    http = inject(HttpClient);

    // questo componente è pensato SOLO per dare una dimostrazione di scambio di token
    // al clic invio nome utente e password di Laura Leone

    // mi viene restituito un token, e in maniera completamente CRIMINALE lo stampo a console
    // sempre in maniere criminale, faccio la chiamata http da qui... questo componente è SOLO DI PROVA

    doTestLogin(){
        this.http.post("http://localhost:8080/sbb/api/users/login", {username:'llaura', password:'pippo'})
        .subscribe(
            json => {
                console.log(json);
                const token = (json as any).token;
                const payload = JSON.parse(atob(token.split('.')[1]));
                console.log('Decoded Token Payload:', payload);
            },  
            error => console.log(error)
        )
    }


}
