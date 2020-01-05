import { Component, OnInit } from '@angular/core';
import {Chart} from 'chart.min.js'
import { EventReportDto } from 'src/app/dto/event_report.dto';

@Component({
  selector: 'app-event-report',
  templateUrl: './event-report.component.html',
  styleUrls: ['./event-report.component.css']
})
export class EventReportComponent implements OnInit {

  data: EventReportDto;
  constructor() { }

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

}
