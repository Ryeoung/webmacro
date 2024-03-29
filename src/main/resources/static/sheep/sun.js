export class Sun {
    constructor() {
        this.radius = 200;

        this.total = 60;
        this.gap = 1 / this.total;

        this.orginPos = [];
        this.changedPos = [];

        for (let idx = 0; idx < this.total; idx++) {
            const pos = this.getCirclePoint(this.radius, this.gap * idx);
            this.orginPos[idx] = pos;
            this.changedPos[idx] = pos;
        }
        this.fps = 30;
        this.fpsTime = 1000 / this.fps;
    }

    resize(stageWidth, stageHeight) {
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;

        this.x = this.stageWidth - this.radius - 140;
        this.y = this.radius + 80;
    }

    draw(ctx, t) {
        if (!this.time) {
            this.time = t;
        }

        const now = t - this.time;
        if (now > this.fpsTime) {
            this.time = t;
            this.updatePoints();
        }

        this.animate(ctx);
    }

    animate(ctx) {
        ctx.fillStyle = '#ffb200';
        ctx.beginPath();

        //ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI);
        let startPos = this.changedPos[0];
        ctx.moveTo(this.x + startPos.x, this.y + startPos.y);
        for(let idx = 1; idx < this.total; idx ++) {
            const pos = this.changedPos[idx];
            ctx.lineTo(this.x + pos.x, this.y + pos.y);
        }

        ctx.closePath();
        ctx.fill();
    }

    updatePoints() {
        for (let idx = 1; idx < this.total; idx++) {
            const pos = this.orginPos[idx];
            this.changedPos[idx] = {
                x: pos.x + this.ranInt(5),
                y: pos.y + this.ranInt(5)
            }
        }
    }

    ranInt(max) {
        return Math.random() * max;
    }
    getCirclePoint(radius, t) {
        const theta = Math.PI * 2 * t;

        return {
            x: Math.cos(theta) * radius,
            y: Math.sin(theta) * radius
        }
    }
}