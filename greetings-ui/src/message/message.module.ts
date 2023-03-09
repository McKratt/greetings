import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { MessageComponent } from './message.component';

const routes: Routes = [{ path: '', component: MessageComponent }];

@NgModule({
  declarations: [MessageComponent],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class MessageModule {}
