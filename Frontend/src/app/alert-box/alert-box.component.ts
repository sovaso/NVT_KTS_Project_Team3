import { Component, OnInit,Output,EventEmitter} from '@angular/core';
import { NgbModal,NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-alert-box',
  templateUrl: './alert-box.component.html',
  styleUrls: ['./alert-box.component.css']
})
export class AlertBoxComponent implements OnInit {
  message: String = '';
  constructor(private modalService: NgbModal,private activeModal: NgbActiveModal) {
  
   }

  ngOnInit() {
  }



  close(){

    //location.reload();
    this.activeModal.dismiss();
    //this.modalService.dismissAll();
    
  }

}
