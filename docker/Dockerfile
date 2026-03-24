# ---------- Builder ----------
FROM golang:1.24-alpine AS builder

RUN apk add --no-cache gcc musl-dev

WORKDIR /app
COPY api/go.mod api/go.sum ./
RUN go mod download

COPY api/ .

RUN --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=1 \
    go build -ldflags="-s -w" -o app

RUN mkdir /app/data

# ---------- Runtime ----------
FROM gcr.io/distroless/base-debian12

WORKDIR /app

COPY --from=builder /app/app .
COPY --from=builder /app/data /app/data

# Distroless already has nonroot user (65532)
USER nonroot:nonroot

EXPOSE 8080
ENTRYPOINT ["./app"]