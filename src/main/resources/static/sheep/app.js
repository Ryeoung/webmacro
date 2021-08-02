import {
    Hill
} from './hill.js';

import {
    SheepController
} from './sheepController.js';

import {
    Sun
} from './sun.js';

class App {
    constructor() {
        this.canvas = document.createElement('canvas');
        this.ctx = this.canvas.getContext('2d');
        document.body.appendChild(this.canvas);
        this.sun = new Sun();
        
        this.hills = [
            new Hill('#fb6dea',0.2, 12),
            new Hill('#ff59c2', 0.8, 8),
            new Hill('#ff4674', 1.4, 6)
        ];
        
        this.sheepController = new SheepController();

        window.addEventListener('resize', this.resize.bind(this), false);
        this.resize();
        
        requestAnimationFrame(this.animate.bind(this));
    }

    resize() {
        this.stageWidth = document.body.clientWidth;
        this.stageHeight = document.body.clientHeight;

        //레티나 디스플레이는 픽셀이 2배
        this.canvas.width = this.stageWidth * 2;
        this.canvas.height = this.stageHeight * 2;
        this.ctx.scale(2,2);
        this.sun.resize(this.stageWidth, this.stageHeight);

        for(let idx = 0; idx < this.hills.length; idx++) {
            this.hills[idx].resize(this.stageWidth, this.stageHeight);
        }

        this.sheepController.resize(this.stageWidth, this.stageHeight);
    }

    animate(t) {
        requestAnimationFrame(this.animate.bind(this));

        this.ctx.clearRect(0, 0, this.stageWidth, this.stageHeight);

        this.sun.draw(this.ctx, t);
        let dots;
        for(let idx = 0; idx < this.hills.length; idx++) {
            dots = this.hills[idx].draw(this.ctx);
        }
        this.sheepController.draw(this.ctx, t, dots);
    }

}

window.onload = () => {
    new App();
}