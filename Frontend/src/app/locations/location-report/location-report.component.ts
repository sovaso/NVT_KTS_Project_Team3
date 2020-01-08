import { Component, OnInit } from '@angular/core';
import {Chart} from 'chart.min.js'
import { LocationReportDto } from 'src/app/dto/location_report.dto';
import { NgbModal,NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-location-report',
  templateUrl: './location-report.component.html',
  styleUrls: ['./location-report.component.css']
})
export class LocationReportComponent implements OnInit {



  data: LocationReportDto;
  constructor(private modalService: NgbModal) { }

  ngOnInit() {
    this.createChart("myChart1", this.data.dailyLabels, this.data.dailyValues)
    this.createChart("myChart2", this.data.weeklyLabels, this.data.weeklyValues)
    this.createChart("myChart3", this.data.monthlyLabels, this.data.monthlyValues)
  }


  createChart(canvasId,labelNames,data){
    var canvas =<HTMLCanvasElement> document.getElementById(canvasId);
    var ctx = canvas.getContext("2d");
    var myChart = new Chart(ctx, {
      type : 'bar',
      data : {
        labels : labelNames,
        datasets : [ {
          label : 'attendance',
          data : data,
          backgroundColor : Array(data.length).fill(' #0099ff'),
          borderColor : Array(data.length).fill('black'),
          borderWidth : 1
        } ]
      },
      options : {
        scales : {
          yAxes : [ {
            ticks : {
              beginAtZero : true
            }
          } ]
        }
      }
    });
  }


    close(){
      this.modalService.dismissAll();
    }



}
