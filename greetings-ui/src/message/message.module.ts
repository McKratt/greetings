import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MessageComponent } from './message.component';

const routes: Routes = [{ path: '', component: MessageComponent }];

@NgModule({
  declarations: [MessageComponent],
  imports: [RouterModule.forChild(routes)],
})
export class MessageModule {}
