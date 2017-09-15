import { Component,NgModule, OnInit, ChangeDetectorRef} from '@angular/core';
//import { customers } from './customers';
import { GridDataResult, PageChangeEvent } from '@progress/kendo-angular-grid';
import {DomSanitizer} from '@angular/platform-browser';
import {MdIconRegistry} from '@angular/material';
import {Http, Headers} from "@angular/http";
import { DropDownsModule } from '@progress/kendo-angular-dropdowns';


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['app.component.scss']



})
export class AppComponent implements OnInit {
    //private gridData: GridDataResult;
    private pageSize: number = 10;
    private skip: number = 0;
    //private data: Object[];
    public data: any[];

    //private items: any[] = customers;
    //private gridData: any[] = customers;
    private gridData: any[] = this.data;
    countries = [
       {id: 1, name: "United States"},
       {id: 2, name: "Australia"},
       {id: 3, name: "Canada"},
       {id: 4, name: "Brazil"},
       {id: 5, name: "England"}
     ];
    selectedValue = null;
    selectedRelValue = null;
    selectedState = null;
    mainPartyFlag = null;
    relPartyFlag = null;
    newRelPartyFlag = null;
    hasRelatedParty:boolean = false;
    client:any = null;
    wcisList: any[] = [];
    buttonShown = null;
    relRecord = null;

    constructor(iconRegistry: MdIconRegistry, sanitizer: DomSanitizer, private http: Http,
        private cd: ChangeDetectorRef) {
    iconRegistry.addSvgIcon(
        'save-svg',
        sanitizer.bypassSecurityTrustResourceUrl('assets/ic_save_black_24px.svg'));
        this.hasRelatedParty = false;
  }
    public selectWcisId(wcisId, ecn){
      //alert("wcisId "+wcisId.value);
      //alert("ecn "+ecn);
      this.selectedValue = wcisId.value;
      this.mainPartyFlag = ecn;
    //  alert('this.hasRelatedParty '+this.hasRelatedParty);
      if (this.hasRelatedParty){

          let y = this.wcisList;
          //alert(' this.wcisList.length '+ this.wcisList.length);
          if (y.length > 0){
          for (let t in y)
          {
              let wcisClientId = y[t].value;

              let z = this.client.customerRelationships;
              //alert('z '+z);
              for (let d in z)
              {
                //alert('d '+d);
                    if (this.client.customerRelationships[d].wcisResponselst != null
                    || this.client.customerRelationships[d].wcisResponselst.length > 0)
                    {

                        let s = this.client.customerRelationships[d].wcisResponselst;
                        //alert('s.length '+s.length);
                         for (let x in s)
                         {
                            //alert('x '+x);
                            let icis =  this.client.customerRelationships[d].wcisResponselst[x].wcisId;
                            if (icis == wcisClientId)
                            {
                                //alert(true);
                                //this.mainPartyFlag = ecn;
                                this.buttonShown = 'true'+ecn;
                            }
                         }

                    }
              }

          }
          }

      }

        if (this.selectedValue == 'new'){

          this.buttonShown = 'true'+ecn;
        }
        else{
        this.checkmatched(wcisId, ecn);
        }


      //alert('test');
      console.log("wcis" );
    }

    private checkmatched(wcisId, ecn):void
    {
    let y = this.wcisList;
    //alert(' this.wcisList.length '+ this.wcisList.length);
    if (y.length > 0){
    for (let t in y)
    {
        let wcisClientId = y[t].value;

        let z = this.client.customerRelationships;
        //alert('z '+z);
        for (let d in z)
        {
          //alert('d '+d);
              if (this.client.customerRelationships[d].wcisResponselst != null
              || this.client.customerRelationships[d].wcisResponselst.length > 0)
              {

                  let s = this.client.customerRelationships[d].wcisResponselst;
                  //alert('s.length '+s.length);
                   for (let x in s)
                   {
                      //alert('x '+x);
                      let icis =  this.client.customerRelationships[d].wcisResponselst[x].wcisId;
                      if (icis == wcisClientId)
                      {
                          //alert(true);
                          //this.mainPartyFlag = ecn;
                          this.buttonShown = 'true'+ecn;
                      }
                   }

              }
        }

    }
    }
    }
    public selectRelWcisId(wcisId, relecn, ecn){
      //alert("wcisId "+wcisId.value);
      //alert("ecn "+ecn);
      this.selectedRelValue = wcisId.value;
      this.relPartyFlag = relecn;
      this.newRelPartyFlag = ecn;
      this.wcisList.push(wcisId);
      if (this.mainPartyFlag == ecn){
        this.buttonShown = 'true'+ecn;

        if(wcisId.value.includes("new")
            || wcisId.value.includes("none")){
          this.buttonShown = false;
        }
      }
      //console.log("wcis" );
    }
    protected pageChange(event: PageChangeEvent): void {
        this.skip = event.skip;
      //  this.loadItems();
    }
    ngOnInit(): void {
    //    this.http.get("./data.json")
      // const headers = new Headers();
      // headers.append('Access-Control-Allow-Origin', '*');
      // headers.append('Access-Control-Allow-Credentials', 'true');
      this.hasRelatedParty = false;
      //this.http.get("http://112.32.4.99:9090/getCustomertarget")
      //this.http.get('assets/data.json')
      this.http.get("http://localhost:9090/getCustomerstaging")
            .subscribe((gridData)=> {
                setTimeout(()=> {
                    this.gridData = gridData.json();

                }, 1000);
            });

    }
    //public updateWcisId(ecn: string){
    public updateWcisId(ecn: string){
        //alert("ecn "+ecn);
        //alert("relatedEcn "+relatedEcn);

        let headers = new Headers();

        headers.append('Content-Type', 'application/json');
        let url = 'http://112.32.4.99:8082/updateCustomer?ecn='+ecn+'&wcisId='+this.selectedValue+'&relatedEcn='+this.relRecord.relatedCustomerECN;
        alert('url '+url);
/*
        this.http
             .post(url, {headers: headers}).subscribe();
            this.http.get("http://112.32.4.99:9090/getCustomertarget")
                   .subscribe((gridData)=> {
                       setTimeout(()=> {
                           this.gridData = gridData.json();
                       }, 1000);
                   });

        this.cd.detectChanges();
        */
    }

  /**  private loadItems(): void {
        this.gridData = {
            data: this.items.slice(this.skip, this.skip + this.pageSize),
            total: this.items.length
        };
      //  alert(this.gridData.data[0]);
    }**/

    public getStatusRowFormat(status)
    {
  //  alert(status);
	  var legendclass = "";
	  if(status.trim() == 'Select WCIS ID'){
		  legendclass+= "multiWcisRow";
    }
    else if(status.trim() == "ECN WCIS Mismatched"){
     legendclass+= "selectRow";
    }
    else if(status.trim() == "SUBMITTED to Cornerstone"){
     legendclass+= "submittedRow";
    }
    //alert(legendclass);
	  return legendclass;
  }
  public clientGridChange(clientGrid, selection){
      //alert("selection.index "+selection.index);
      let selectedClient = clientGrid.data.data[selection.index];
      this.client = selectedClient;
      this.hasRelatedParty = selectedClient.customerRelationships.length > 0;

  }

  public relationshipGridChange(relationshipGrid, selection){
       alert('relationshipGridChange ');
       alert('selection.index '+selection.index);
       let selectedRecord = relationshipGrid.data[selection.index];
       this.relRecord = selectedRecord;
       //alert('this.relRecord.relatedCustomerECN '+this.relRecord.relatedCustomerECN);
  }
}
