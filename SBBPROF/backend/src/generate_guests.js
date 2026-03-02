const fs = require('fs');

const firstNames = ["Mario", "Luigi", "Giovanni", "Paolo", "Anna", "Maria", "Elena", "Giulia", "Francesco", "Alessandro", "Roberto", "Stefano", "Laura", "Chiara", "Federica", "Valentina", "Andrea", "Marco", "Luca", "Matteo"];
const lastNames = ["Rossi", "Bianchi", "Verdi", "Ferrari", "Esposito", "Ricci", "Romano", "Colombo", "Bruno", "Gallo", "Conti", "De Luca", "Mancini", "Costa", "Giordano", "Rizzo", "Lombardi", "Moretti", "Barbieri", "Fontana"];
const cities = ["Roma", "Milano", "Napoli", "Torino", "Palermo", "Genova", "Bologna", "Firenze", "Bari", "Catania", "Venezia", "Verona", "Messina", "Padova", "Trieste"];

function getRandomInt(max) {
  return Math.floor(Math.random() * max);
}

function getRandomDate(start, end) {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
}

function generateSSN(firstName, lastName, dob) {
    // Generazione SSN fittizia ma verosimile (16 caratteri)
    // Formato: 3 cognome + 3 nome + 2 anno + 1 mese + 2 giorno + 4 comune + 1 check
    const c1 = lastName.substring(0, 3).padEnd(3, 'X').toUpperCase();
    const c2 = firstName.substring(0, 3).padEnd(3, 'X').toUpperCase();
    const year = dob.getFullYear().toString().substring(2);
    const month = String.fromCharCode(65 + dob.getMonth()); // A, B, C... approssimativo
    const day = dob.getDate().toString().padStart(2, '0');
    
    // Parte finale casuale per arrivare a 16 char (H501 è il codice catastale di Roma)
    const randomChar = String.fromCharCode(65 + getRandomInt(26));
    
    let ssn = c1 + c2 + year + month + day + "H501" + randomChar;
    return ssn.substring(0, 16);
}

let sqlOutput = "";

for (let i = 0; i < 2000; i++) {
    const firstName = firstNames[getRandomInt(firstNames.length)];
    const lastName = lastNames[getRandomInt(lastNames.length)];
    const city = cities[getRandomInt(cities.length)];
    const address = `Via ${lastName} ${getRandomInt(100) + 1}`;
    
    // Data di nascita tra il 1950 e il 2003
    const dob = getRandomDate(new Date(1950, 0, 1), new Date(2003, 0, 1));
    const dobStr = dob.toISOString().split('T')[0];
    
    const ssn = generateSSN(firstName, lastName, dob);

    // Nota: Assumo che le colonne DB siano in snake_case come da default Hibernate (first_name, last_name)
    // Se hai cambiato la naming strategy, adatta i nomi delle colonne qui sotto.
    sqlOutput += `INSERT INTO guest (first_name, last_name, ssn, dob, address, city) VALUES ('${firstName}', '${lastName}', '${ssn}', '${dobStr}', '${address}', '${city}');\n`;
}

// Stampa a console (puoi ridirigere l'output su file con > guests.sql)
console.log(sqlOutput);
// Oppure decommenta per scrivere direttamente su file:
// fs.writeFileSync('guests.sql', sqlOutput);
