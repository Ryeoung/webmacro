import {
    Sheep
} from './sheep.js';
export class SheepController {
    constructor() {
        this.image = new Image();
        this.image.onload = () => {
            this.load();
        };
        this.image.src = '/parking/resources/sheep/img/sheep.png';

        this.items = [];

        this.cur = 0;
        this.isLoaded = false;
    }

    resize(stageWidth, stageHeight) {
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;
    }

    load() {
        this.isLoaded = true;
        this.addSheep();
    }
    
    addSheep() {
        this.items.push(
            new Sheep(this.image, this.stageWidth)
        );
    }
    draw(ctx, t, dots) {
        if(this.isLoaded) {
            this.cur += 1;

            if (this.cur > 200) {
                this.cur = 0;
                this.addSheep();
            }

            for(let idx = this.items.length - 1; idx >= 0; idx--) {
                const item  = this.items[idx];

                if(item.x < -item.width) {
                    this.items.splice(idx, 1);
                } else {
                    item.draw(ctx, t, dots);
                }
            }
        }
    }
}