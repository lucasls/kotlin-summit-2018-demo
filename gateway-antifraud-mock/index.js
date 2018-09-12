const express = require('express')
const delay = require('delay')
const uuidv4 = require('uuid/v4');

const app = express()
const port = 3000

function randomInt(from, to) {
    return from + Math.floor(Math.random() * (to - from))
}

app.use(express.json())

app.post('/antifraud/validate', async (request, response) => {
    await delay(randomInt(400, 800))

    const r = Math.random()
    let status

    if (r < 0.95) {
        status = "APPROVED"
    } else if (r < 0.98) {
        status = "REPROVED"
    } else {
        status = "FAILED"
    }
    
    response.send({
        "status": status,
        "reference": request.body.reference
    })
})

app.post('/gateway/authorize', async (request, response) => {
    await delay(randomInt(6000, 8000))

    const r = Math.random()
    let status

    if (r < 0.85) {
        status = "AUTHORIZED"
    } else if (r < 0.95) {
        status = "DECLINED"
    } else {
        status = "FAILED"
    }

    let tid
    if (status == "AUTHORIZED") {
        tid = uuidv4()
    } else {
        tid = null
    }

    response.send({
        "status": status,
        "tid": tid,
        "reference": request.body.reference
    })
})

app.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }

    console.log(`server is listening on ${port}`)
})
