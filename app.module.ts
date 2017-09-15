import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GridModule } from '@progress/kendo-angular-grid';
import { AppComponent } from './app.component';
import { LayoutModule } from '@progress/kendo-angular-layout';
import { FormsModule } from '@angular/forms';
import { HttpModule } from "@angular/http";
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { DropDownsModule } from '@progress/kendo-angular-dropdowns';
import {MdButtonModule, MdCheckboxModule, MdSelectModule, MdInputModule,MdToolbarModule, MdIconModule} from '@angular/material';

@NgModule({
   bootstrap:    [AppComponent],
   declarations: [AppComponent],
   imports:      [BrowserModule, FormsModule,BrowserAnimationsModule,MdIconModule,
     GridModule,LayoutModule,NoopAnimationsModule, MdSelectModule, MdInputModule, MdToolbarModule,
    HttpModule,DropDownsModule]
})
export class AppModule {
}
