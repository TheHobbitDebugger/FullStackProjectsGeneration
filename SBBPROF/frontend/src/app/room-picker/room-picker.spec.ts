import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomCategoryPicker } from './room-category-picker';

describe('RoomCategoryPicker', () => {
  let component: RoomCategoryPicker;
  let fixture: ComponentFixture<RoomCategoryPicker>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoomCategoryPicker]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoomCategoryPicker);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
