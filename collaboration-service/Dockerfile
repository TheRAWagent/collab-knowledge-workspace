#syntax=docker/dockerfile:1

# === Base stage ===
FROM dhi.io/bun:1.3-alpine3.22-dev AS base
WORKDIR /usr/src/app

# === Install dependencies into temp directory ===
FROM base AS install
RUN mkdir -p /temp/dev
COPY package.json bun.lock /temp/dev/
RUN cd /temp/dev && bun install --frozen-lockfile

# Install with --production (exclude devDependencies)
RUN mkdir -p /temp/prod
COPY package.json bun.lock /temp/prod/
RUN cd /temp/prod && bun install --frozen-lockfile --production

# === Prerelease stage ===
FROM base AS prerelease
COPY --from=install /temp/dev/node_modules node_modules
COPY . .

# [Optional] Tests & build
ENV NODE_ENV=production
# RUN bun test
RUN bun orval
RUN bun run build

# === Release stage ===
FROM dhi.io/bun:1-alpine3.22
WORKDIR /usr/src/app

COPY --from=install /temp/prod/node_modules node_modules
COPY --from=prerelease /usr/src/app/dist ./dist
COPY --from=prerelease /usr/src/app/package.json .

EXPOSE 8080 9091

CMD ["bun", "dist/index.js"]