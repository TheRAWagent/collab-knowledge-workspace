export interface ProseMirrorDoc {
    type: 'doc'
    content: ProseMirrorNode[]
}

export interface ProseMirrorNode {
    type: string
    attrs?: Record<string, any>
    content?: ProseMirrorNode[]
    marks?: {type: string; attrs?: Record<string, any>}[]
    text?: string
}
