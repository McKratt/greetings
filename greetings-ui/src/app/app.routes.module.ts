import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
  {
    path: 'message',
    loadChildren: () =>
      import('../message/message.module').then((m) => m.MessageModule),
  },
  {
    path: '**',
    redirectTo: 'message',
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { initialNavigation: 'enabledBlocking' }),
  ],
  exports: [RouterModule],
})
export class AppRoutesModule {}
