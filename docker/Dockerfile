# ---------- Builder ----------
FROM golang:1.25-bookworm AS builder

WORKDIR /app

COPY api/go.mod api/go.sum ./
RUN --mount=type=cache,target=/go/pkg/mod go mod download

COPY api/ .

RUN --mount=type=cache,target=/root/.cache/go-build \
    CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build -ldflags="-s -w" -o app

# ---------- Runtime ----------
FROM gcr.io/distroless/cc-debian12

WORKDIR /app

COPY --from=builder /app/app .

USER nonroot:nonroot

EXPOSE 8080

ENTRYPOINT ["./app"]