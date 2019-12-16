import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-alert-box',
  templateUrl: './alert-box.component.html',
  styleUrls: ['./alert-box.component.css']
})
export class AlertBoxComponent implements OnInit {
  message: String = '';
  constructor() {
  
   }

  ngOnInit() {
  }

  close(){
    location.reload();
  }

}
